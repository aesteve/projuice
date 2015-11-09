package unit;

import io.projuice.model.ProjuiceUser;
import io.projuice.services.MapTokenService;
import io.projuice.services.TokenService;
import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
	public void tearDown(TestContext context) {
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

	@Test
	public void clearTokens(TestContext context) {
		Async async = context.async();
		tokenService.createTokenFor(someone, res -> {
			context.assertFalse(res.failed());
			String token1 = res.result();
			context.assertNotNull(token1);
			tokenService.createTokenFor(someone, res2 -> {
				String token2 = res.result();
				context.assertNotNull(token2);
				context.assertFalse(res2.failed());
				tokenService.clearFor(someone);
				tokenService.getTokenFor(someone, res3 -> {
					context.assertTrue(res3.failed());
					context.assertNull(res3.result());
					tokenService.getUserAssociatedWith(token1, res4 -> {
						context.assertTrue(res4.failed());
						context.assertNull(res4.result());
						tokenService.getUserAssociatedWith(token1, res5 -> {
							context.assertTrue(res5.failed());
							context.assertNull(res5.result());
							async.complete();
						});
					});
				});
			});
		});
	}

}
