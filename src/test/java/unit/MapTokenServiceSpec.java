package unit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.projuice.model.ProjuiceUser;
import io.projuice.services.MapTokenService;
import io.projuice.services.TokenService;
import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class MapTokenServiceSpec {

	private static ProjuiceUser someone;
	private static TokenService tokenService;

	@Before
	public void setUp(TestContext context) {
		someone = new ProjuiceUser();
		someone.setUsername("someone");
		someone.setEmailAddress("someone@somewhere.com");
		someone.setPassword("shhhh");
		tokenService = new MapTokenService();
		Future<Void> fut = Future.future();
		fut.setHandler(context.asyncAssertSuccess());
		tokenService.start(fut);
	}

	@After
	public void tearDow(TestContext context) {
		Future<Void> fut = Future.future();
		fut.setHandler(context.asyncAssertSuccess());
		tokenService.stop(fut);
	}

	@Test
	public void createToken(TestContext context) {
		Async async = context.async();
		tokenService.createTokenFor(someone, res -> {
			context.assertFalse(res.failed());
			String token = res.result();
			context.assertNotNull(token);
			tokenService.getTokenFor(someone, res2 -> {
				context.assertFalse(res2.failed());
				context.assertEquals(token, res2.result());
				tokenService.getUserAssociatedWith(token, res3 -> {
					context.assertFalse(res3.failed());
					context.assertEquals(someone, res3.result());
					async.complete();
				});
			});
		});
	}

}
