
import buildFeed
import com.somanyfeeds.aggregator.FeedPresenter
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedType
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeedPresenterSpec: Spek() { init {
    val presenter = FeedPresenter()

    given("some feeds and a selected feed") {
        val blog = buildFeed(name = "My Blog", slug = "blog")
        val github = buildFeed(name = "My Github", slug = "github")
        val slugs = setOf("blog", "photos")

        on("creation of a selected feed") {
            val blogViewModel = presenter.present(blog, slugs)

            it("sets up the presenter") {
                assertEquals("My Blog", blogViewModel.name)
                assertEquals("/photos", blogViewModel.path)
                assertTrue(blogViewModel.isSelected)
            }
        }

        on("creation of a feed that's not selected") {
            val githubViewModel = presenter.present(github, slugs)

            it("sets up the presenter") {
                assertEquals("My Github", githubViewModel.name)
                assertEquals("/blog,github,photos", githubViewModel.path)
                assertFalse(githubViewModel.isSelected)
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
