package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import io.projuice.annotations.ProjectRoleCheck;
import io.projuice.model.Issue;
import io.projuice.model.Role;
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
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PATCH;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PUT;
import com.github.aesteve.vertx.nubes.context.PaginationContext;
import com.github.aesteve.vertx.nubes.exceptions.ValidationException;
import com.github.aesteve.vertx.nubes.exceptions.http.impl.BadRequestException;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

@Controller("/api/1/projects/:projectId/issues")
@ContentType("application/json")
@Auth(method = API_TOKEN, authority = LOGGED_IN)
public class IssueApiController extends CheckController {

	@GET
	@RetrieveByQuery
	@ProjectRoleCheck(Role.MEMBER)
	public FindBy<Issue> getProjectIssues(
			PaginationContext pageContext,
			@Param String projectId,
			@Param IssueStatus status,
			@Param IssueType type) {

		FindBy<Issue> findBy = new FindBy<>(Issue.class, "projectId", projectId);
		if (status != null) {
			findBy.eq("status", status);
		}
		if (type != null) {
			findBy.eq("type", type);
		}
		return findBy;
	}

	@POST
	@Create
	@ProjectRoleCheck(Role.MEMBER)
	public Issue openIssue(@RequestBody Issue issue, @Param String projectId) throws BadRequestException {
		issue.setProjectId(projectId);
		try {
			issue.validate();
		} catch (ValidationException ve) {
			throw new BadRequestException(ve);
		}

		issue.generateId();
		return issue;
	}

	@GET("/:issueId/")
	@RetrieveById
	@ProjectRoleCheck(Role.MEMBER)
	public FindBy<Issue> getIssue(@Param String issueId) {
		return new FindBy<>(Issue.class, "id", issueId);
	}

	@PUT("/:issueId/")
	@PATCH("/:issueId/")
	@Update
	@ProjectRoleCheck(Role.MEMBER)
	public void updateIssue(
			RoutingContext context,
			Payload<UpdateBy<Issue>> payload,
			@RequestBody Issue issue,
			@Param String issueId) throws BadRequestException {

		try {
			issue.validate();
		} catch (ValidationException ve) {
			throw new BadRequestException(ve);
		}
		checkIssueExists(context, issueId, existingIssue -> {
			payload.set(new UpdateBy<>(issue, "id", issueId));
			context.next();
		});
	}

	@PUT("/:issueId/close")
	@PATCH("/:issueId/close")
	@Update
	public void closeIssue(
			RoutingContext context,
			Payload<UpdateBy<Issue>> payload,
			@Param String projectId,
			@Param String issueId) {

		checkIssueExists(context, issueId, issue -> {
			issue.close();
			payload.set(new UpdateBy<>(issue, "id", issueId));
			context.next();
		});

	}

	@PUT("/:issueId/assign")
	@PATCH("/:issueId/assign")
	@Update
	@ProjectRoleCheck(Role.MEMBER)
	public void assignIssue(
			RoutingContext context,
			Payload<UpdateBy<Issue>> payload,
			@Param(mandatory = true) String assignee,
			@Param String projectId,
			@Param String issueId) {

		checkIssueExists(context, issueId, issue -> {
			checkUserExists(context, assignee, user -> {
				issue.assign(user);
				payload.set(new UpdateBy<>(issue, "id", issueId));
				context.next();
			});
		});

	}

}
