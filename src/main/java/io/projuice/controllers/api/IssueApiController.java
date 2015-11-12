package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import io.projuice.model.Issue;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.issue.IssueStatus;
import io.projuice.model.issue.IssueType;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.orm.annotations.Update;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.nubes.orm.queries.UpdateBy;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.auth.User;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PATCH;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PUT;
import com.github.aesteve.vertx.nubes.exceptions.ValidationException;
import com.github.aesteve.vertx.nubes.exceptions.http.impl.BadRequestException;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

@Controller("/api/1/projects/:projectId/issues")
@ContentType("application/json")
public class IssueApiController extends CheckController {

	@GET
	@RetrieveByQuery
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void getProjectIssues(
			RoutingContext context,
			@User ProjuiceUser currentUser,
			Payload<FindBy<Issue>> payload,
			@Param String projectId,
			@Param IssueStatus status,
			@Param IssueType type) {

		checkUserHasAccessToProject(context, currentUser, projectId, project -> {
			FindBy<Issue> findBy = new FindBy<>(Issue.class, "projectId", projectId);
			if (status != null) {
				findBy.eq("status", status);
			}
			if (type != null) {
				findBy.eq("type", type);
			}
			payload.set(findBy);
			context.next();
		});

	}

	@POST
	@Create
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void openIssue(
			RoutingContext context,
			@User ProjuiceUser currentUser,
			Payload<Issue> payload,
			@Param String projectId,
			@RequestBody Issue issue) throws BadRequestException {

		try {
			issue.validate();
		} catch (ValidationException ve) {
			throw new BadRequestException(ve);
		}

		checkUserHasAccessToProject(context, currentUser, projectId, project -> {
			issue.generateId();
			payload.set(issue);
			context.next();
		});

	}

	@GET("/:issueId/")
	@RetrieveById
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void getIssue(
			RoutingContext context,
			Payload<FindBy<Issue>> payload,
			@User ProjuiceUser currentUser,
			@Param String projectId,
			@Param String issueId) {

		checkUserHasAccessToProject(context, currentUser, projectId, project -> {
			payload.set(new FindBy<>(Issue.class, "id", issueId));
			context.next();
		});

	}

	@PUT("/:issueId/")
	@PATCH("/:issueId/")
	@Update
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void updateIssue(
			RoutingContext context,
			Payload<UpdateBy<Issue>> payload,
			@User ProjuiceUser currentUser,
			@RequestBody Issue issue,
			@Param String projectId,
			@Param String issueId) throws BadRequestException {

		try {
			issue.validate();
		} catch (ValidationException ve) {
			throw new BadRequestException(ve);
		}

		checkUserHasAccessToProject(context, currentUser, projectId, project -> {
			issue.setId(issueId);
			payload.set(new UpdateBy<>(issue, "id", issueId));
			context.next();
		});

	}

	@PUT("/:issueId/close")
	@PATCH("/:issueId/close")
	@Update
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void closeIssue(
			RoutingContext context,
			Payload<UpdateBy<Issue>> payload,
			@User ProjuiceUser currentUser,
			@Param String projectId,
			@Param String issueId) {

		checkUserHasAccessToProject(context, currentUser, projectId, project -> {
			checkIssueExists(context, issueId, issue -> {
				issue.close();
				issue.setId(issueId);
				payload.set(new UpdateBy<>(issue, "id", issueId));
				context.next();
			});
		});

	}

	@PUT("/:issueId/assign")
	@PATCH("/:issueId/assign")
	@Update
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void assignIssue(
			RoutingContext context,
			Payload<UpdateBy<Issue>> payload,
			@User ProjuiceUser currentUser,
			@Param(mandatory = true) String assignee,
			@Param String projectId,
			@Param String issueId) {

		checkUserHasAccessToProject(context, currentUser, projectId, project -> {
			checkIssueExists(context, issueId, issue -> {
				checkUserExists(context, assignee, user -> {
					issue.assign(user);
					payload.set(new UpdateBy<>(issue, "id", issueId));
					context.next();
				});
			});
		});

	}

}
