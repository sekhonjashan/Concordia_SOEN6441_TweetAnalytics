package modules;

import com.google.inject.AbstractModule;

import play.libs.akka.AkkaGuiceSupport;
import service.TwitterService;
import service.TwitterServiceImpl;

/**
 * The module class is used for dependency injection.
 * 
 * @author Created by Ashwin Soorkeea.
 * @since V_1.1
 * @version V1
 *
 */
public class TwitterModule extends AbstractModule implements AkkaGuiceSupport {

	/**
	 * Binds the <code>TwitterServiceImpl</code> to the <code>TwitterService</code>
	 * interface.
	 */
	@Override
	protected final void configure() {
		bind(TwitterService.class).to(TwitterServiceImpl.class);
	}

}
