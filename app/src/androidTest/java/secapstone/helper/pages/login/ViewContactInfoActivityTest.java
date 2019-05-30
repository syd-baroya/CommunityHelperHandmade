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
public class ViewContactInfoActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void viewContactInfoActivityTest() {
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
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.linearLayout4),
                        childAtPosition(
                                allOf(withId(R.id.profileButtonContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                0)));
        linearLayout.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.logPaymentTitle), withText("7085990465"),
                        childAtPosition(
                                allOf(withId(R.id.modalContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("7085990465")));

        ViewInteraction button = onView(
                allOf(withId(R.id.callNowButton),
                        childAtPosition(
                                allOf(withId(R.id.modalContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.textNowButton),
                        childAtPosition(
                                allOf(withId(R.id.modalContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                4),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.artisan_address), withText("8056 S McVicker Ave, Burbank IL USA, 60459"),
                        childAtPosition(
                                allOf(withId(R.id.modalContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                7),
                        isDisplayed()));
        textView2.check(matches(withText("8056 S McVicker Ave, Burbank IL USA, 60459")));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.getDirectionsButton),
                        childAtPosition(
                                allOf(withId(R.id.modalContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                8),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.getDirectionsButton),
                        childAtPosition(
                                allOf(withId(R.id.modalContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                8),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));
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
