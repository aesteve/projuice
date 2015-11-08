package io.projuice.verticles;

import com.github.aesteve.vertx.nubes.annotations.services.Verticle;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import integration.ProjuiceTestBase;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

@Verticle(inheritsConfig = true)
public class EmbeddedMongoVerticle extends AbstractVerticle {
	
	private MongodExecutable mongod;

	@Override
	public void start(Future<Void> future) {
		MongodStarter starter = MongodStarter.getDefaultInstance();
		try {
			int port = ProjuiceTestBase.MONGO_PORT;
			MongodConfigBuilder builder = new MongodConfigBuilder();
			builder.version(Version.Main.PRODUCTION);
			builder.net(new Net(port, Network.localhostIsIPv6()));
			mongod = starter.prepare(builder.build());
			mongod.start();
		} catch (Exception ioe) {
			future.fail(ioe);
			return;
		}
		future.complete();
	}

	@Override
	public void stop(Future<Void> future) {
		if (mongod != null) {
			mongod.stop();
			mongod = null;
		}
		future.complete();
	}

}
