package teammates.test.cases.ui;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import teammates.common.datatransfer.DataBundle;
import teammates.common.datatransfer.FeedbackSessionAttributes;
import teammates.common.datatransfer.InstructorAttributes;
import teammates.common.util.Const;
import teammates.ui.controller.AjaxResult;
import teammates.ui.controller.InstructorFeedbackResultsByGRQSeeMorePageAction;
import teammates.ui.controller.InstructorFeedbackResultsByGRQSeeMorePageData;

public class InstructorFeedbackResultsByGRQSeeMorePageActionTest extends
        BaseActionTest {
    private final DataBundle dataBundle = getTypicalDataBundle();

    @BeforeClass
    public static void classSetUp() throws Exception {
        printTestClassHeader();
		restoreTypicalDataInDatastore();
        uri = Const.ActionURIs.INSTRUCTOR_FEEDBACK_RESULTS_AJAX_BY_GRQ;
    }
    
    @Test
    public void testExcecuteAndPostProcess() throws Exception {
        FeedbackSessionAttributes fs = dataBundle.feedbackSessions.get("session1InCourse1");
        
        InstructorAttributes instructor1 = dataBundle.instructors.get("instructor1OfCourse1");
        
        gaeSimulation.loginAsInstructor(instructor1.googleId);
        
        ______TS("Unsuccessful case: not enough parameters");
        
        verifyAssumptionFailure();
        
        String[] submissionParams = new String[]{
                Const.ParamsNames.COURSE_ID, fs.courseId,
                Const.ParamsNames.FEEDBACK_SESSION_NAME, fs.feedbackSessionName
        };
        
        verifyAssumptionFailure(submissionParams);
        
        ______TS("typical successful case");
        
        submissionParams = new String[]{
                Const.ParamsNames.COURSE_ID, fs.courseId,
                Const.ParamsNames.FEEDBACK_SESSION_NAME, fs.feedbackSessionName,
                Const.ParamsNames.SECTION_NAME, "Section 1"
        };
        
        InstructorFeedbackResultsByGRQSeeMorePageAction action = getAction(submissionParams);
        AjaxResult result = (AjaxResult) action.executeAndPostProcess();
        InstructorFeedbackResultsByGRQSeeMorePageData data = 
                (InstructorFeedbackResultsByGRQSeeMorePageData) result.data;
        assertEquals(5, data.answer.values().size());
    }
    
    private InstructorFeedbackResultsByGRQSeeMorePageAction getAction(String... params) throws Exception {
        return (InstructorFeedbackResultsByGRQSeeMorePageAction) (gaeSimulation.getActionObject(uri, params));
    }
}