package secapstone.helper.pages.login;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
public class ViewReportActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void viewReportActivityTest() {
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

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.linearLayout3),
                        childAtPosition(
                                allOf(withId(R.id.profileButtonContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                1)));
        linearLayout.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.reportInfo), withText("Shipment of Products"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Shipment of Products")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.reportInfo), withText("Product Purchases"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Product Purchases")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.reportInfo), withText("Date                  Amount   Product ID                  Artisan ID"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Date                  Amount   Product ID                  Artisan ID")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.reportInfo), withText("Date                  Amount   Product ID                  Artisan ID"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Date                  Amount   Product ID                  Artisan ID")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.reportInfo), withText("05/29/2019     1.0          Dzo9a8mg4tsR3w6Lzjzc pxgylfYM7MhPeEyyLLfv"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("05/29/2019     1.0          Dzo9a8mg4tsR3w6Lzjzc pxgylfYM7MhPeEyyLLfv")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.reportInfo), withText("05/29/2019     40.0        Dzo9a8mg4tsR3w6Lzjzc pxgylfYM7MhPeEyyLLfv"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("05/29/2019     40.0        Dzo9a8mg4tsR3w6Lzjzc pxgylfYM7MhPeEyyLLfv")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.reportInfo), withText("05/29/2019     40.0        Dzo9a8mg4tsR3w6Lzjzc pxgylfYM7MhPeEyyLLfv"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(R.id.listing_list_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("05/29/2019     40.0        Dzo9a8mg4tsR3w6Lzjzc pxgylfYM7MhPeEyyLLfv")));
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
