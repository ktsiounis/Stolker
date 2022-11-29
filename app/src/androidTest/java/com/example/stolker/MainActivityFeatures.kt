import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.stolker.MainActivityRobot.Companion.mainActivity

import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityFeatures {

    @Test
    fun when_a_product_from_the_list_is_clicked_then_its_details_are_displayed() {
        mainActivity() {
            waitOnView(2000)
            recyclerViewIsDisplayed()
            recyclerViewClickOnItemGold()
            productGoldDetailsDisplayed()
        }
    }

    @Test
    fun when_search_for_a_product_and_exists_then_its_details_are_displayed() {
        mainActivity() {
            clickSearchFab()
            enterProductIdAndSearch("sb26500")
            productGoldDetailsDisplayed()
        }
    }

    @Test
    fun when_search_for_a_product_and_does_not_exist_then_error_message_displayed() {
        mainActivity() {
            clickSearchFab()
            enterProductIdAndSearch("26500")
            waitOnView(1000)
            errorMessageIsDisplayed("Not Found!")
        }
    }

}
