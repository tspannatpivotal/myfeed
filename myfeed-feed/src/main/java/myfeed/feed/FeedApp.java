package myfeed.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Spencer Gibb
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class FeedApp {
	@Autowired
	private FeedItemRepository repo;

	@Autowired
	private UserService user;

	@RequestMapping(value = "/@{username}", method = GET)
	public Page<FeedItem> getUser(@PathVariable("username") String username) {
		Page<FeedItem> items = repo.findByUseridOrderByCreatedDesc(user.findId(username),
				new PageRequest(0, 20));
		return items;
	}

	@RequestMapping(value = "/@@{username}", method = GET)
	public PagedResources<FeedItem> getUserResource(@PathVariable("username") String username) {
		Page<FeedItem> items = repo.findByUseridOrderByCreatedDesc(user.findId(username),
				new PageRequest(0, 20));
		return new PagedResources<>(items.getContent(), getMetadata(items));
	}

	public static PagedResources.PageMetadata getMetadata(Page<?> page) {
		return new PagedResources.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
	}

	public static void main(String[] args) {
		SpringApplication.run(FeedApp.class, args);
	}
}
