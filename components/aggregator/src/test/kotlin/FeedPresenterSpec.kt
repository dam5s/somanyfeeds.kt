
import buildFeed
import com.somanyfeeds.aggregator.FeedPresenter
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedType
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeedPresenterSpec: Spek() { init {
    given("some feeds and a selected feed") {
        val blog = buildFeed(name = "My Blog", slug = "blog")
        val photos = buildFeed(name = "My Photos", slug = "photos")
        val github = buildFeed(name = "My Github", slug = "github")

        val allFeeds = listOf(blog, photos, github)
        val slugs = setOf("blog", "photos")

        on("creation of a selected feed") {
            val blogPresenter = FeedPresenter(blog, slugs)

            it("sets up the presenter") {
                assertEquals("My Blog", blogPresenter.name)
                assertEquals("/photos", blogPresenter.path)
                assertTrue(blogPresenter.isSelected)
            }
        }

        on("creation of a feed that's not selected") {
            val githubPresenter = FeedPresenter(github, slugs)

            it("sets up the presenter") {
                assertEquals("My Github", githubPresenter.name)
                assertEquals("/blog,github,photos", githubPresenter.path)
                assertFalse(githubPresenter.isSelected)
            }
        }
    }
}}

fun buildFeed(
    id: Long? = null,
    name: String = "Blog",
    slug: String = "blog",
    url: String = "http://example.com/feed",
    type: FeedType = FeedType.RSS
) = Feed(id = id, name = name, slug = slug, type = type, url = url)
