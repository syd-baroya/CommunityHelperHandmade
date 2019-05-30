package secapstone.helper.pages.login;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import secapstone.helper.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogPayment2ActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void logPayment2ActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_with_amazon), withText("    Login With Amazon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_login),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.artisan_list_parent),
                        childAtPosition(
                                allOf(withId(R.id.artisan_recycler_view),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                2),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.logPaymentButton), withText("Log Payment"),
                        childAtPosition(
                                allOf(withId(R.id.logPaymentContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                2)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.logPaymentButton), withText("Log Payment to Diane Erin"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.confirmButton), withText("Confirm Payment"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.constraintLayout7),
                                        0),
                                6),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.text_view_title), withText("Purse"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Purse")));

        ViewInteraction button = onView(
                allOf(withId(R.id.logShipmentButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.purchaseButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                2),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.moneyOwed), withText("$364.50"),
                        childAtPosition(
                                allOf(withId(R.id.logPaymentContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                2)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("$364.50")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
