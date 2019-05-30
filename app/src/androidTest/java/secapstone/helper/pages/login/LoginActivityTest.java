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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_with_amazon), withText("    Login With Amazon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_login),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.searchActionItemsField), withText("Search Artisans"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("Search Artisans")));

        ViewInteraction button = onView(
                allOf(withId(R.id.addArtisanButton),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.smallLabel), withText("Action Items"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.navigation_action_items), withContentDescription("Action Items")),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Action Items")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.largeLabel), withText("Artisans"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.navigation_artisans), withContentDescription("Artisans")),
                                        1),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Artisans")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.smallLabel), withText("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        allOf(withId(R.id.navigation_profile), withContentDescription("Profile")),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Profile")));
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
