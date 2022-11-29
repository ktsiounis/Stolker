package com.example.stolker

import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.stolker.ui.activities.MainActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf

internal class MainActivityRobot {

    companion object {
        fun mainActivity(function: MainActivityRobot.() -> Unit) = MainActivityRobot().apply {
            val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
            val scenario = ActivityScenario.launch<MainActivity>(intent)
            function()
            scenario.close()
        }
    }

    fun recyclerViewIsDisplayed() {
        onView(withId(R.id.products_recycler_view))
            .check(matches(isDisplayed()))
    }

    fun recyclerViewClickOnItemGold() {

        onView(withId(R.id.products_recycler_view))
            .perform(
                RecyclerViewActions.scrollTo<ViewHolder>(
                    hasDescendant(withText("Gold"))
                )
            )

        onView(withId(R.id.products_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<ViewHolder>(
                    hasDescendant(withText("Gold")),
                    click()
                )
            )
    }

    fun clickSearchFab() {
        onView(withId(R.id.search_manually_fab))
            .perform(click())
    }

    fun enterProductIdAndSearch(id: String) {
        onView(withId(R.id.manual_product_id_edittext))
            .perform(
                replaceText(id),
                closeSoftKeyboard()
            )

        onView(withText("Search"))
            .perform(click())
    }

    fun productGoldDetailsDisplayed() {
        onView(withId(R.id.current_price))
            .check(matches(isDisplayed()))

        onView(withId(R.id.product_title))
            .check(matches(withText("Gold")))
    }

    fun errorMessageIsDisplayed(message: String) {
        onView(
            anyOf(
                withText(message),
                withText("Something unexpected happen")
            )
        )
        .check(matches(isDisplayed()))
    }

    fun waitOnView(delayInMillis: Long) {
        onView(isRoot()).perform(waitFor(delayInMillis))
    }

    private fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

}