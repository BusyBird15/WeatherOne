package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.collect.Lists;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.WebServiceUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SimpleObject
@DesignerComponent(category = ComponentCategory.INTERNAL, designerHelpDescription = "<p>The Voting component enables users to vote on a question by communicating with a Web service to retrieve a ballot and later sending back users' votes.</p>", iconName = "images/voting.png", nonVisible = true, version = 1)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class Voting extends AndroidNonvisibleComponent implements Component {
    private static final String BALLOT_OPTIONS_PARAMETER = "options";
    private static final String BALLOT_QUESTION_PARAMETER = "question";
    private static final String ID_REQUESTED_PARAMETER = "idRequested";
    private static final String IS_POLLING_PARAMETER = "isPolling";
    private static final String LOG_TAG = "Voting";
    private static final String REQUESTBALLOT_COMMAND = "requestballot";
    private static final String SENDBALLOT_COMMAND = "sendballot";
    private static final String USER_CHOICE_PARAMETER = "userchoice";
    private static final String USER_ID_PARAMETER = "userid";
    private Activity activityContext;
    /* access modifiers changed from: private */
    public Handler androidUIHandler = new Handler();
    /* access modifiers changed from: private */
    public ArrayList<String> ballotOptions = new ArrayList<>();
    /* access modifiers changed from: private */
    public String ballotOptionsString;
    /* access modifiers changed from: private */
    public String ballotQuestion = "";
    /* access modifiers changed from: private */
    public Boolean idRequested = false;
    /* access modifiers changed from: private */
    public Boolean isPolling = false;
    private String serviceURL = "http://androvote.appspot.com";
    private ComponentContainer theContainer;
    /* access modifiers changed from: private */
    public String userChoice = "";
    /* access modifiers changed from: private */
    public String userId = "";

    public Voting(ComponentContainer container) {
        super(container.$form());
        this.theContainer = container;
        this.activityContext = container.$context();
        this.serviceURL = "http://androvote.appspot.com";
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The URL of the Voting service")
    public String ServiceURL() {
        return this.serviceURL;
    }

    @DesignerProperty(defaultValue = "http://androvote.appspot.com", editorType = "string")
    @SimpleProperty
    public void ServiceURL(String serviceURL2) {
        this.serviceURL = serviceURL2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The question to be voted on.")
    public String BallotQuestion() {
        return this.ballotQuestion;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The list of ballot options.")
    public List<String> BallotOptions() {
        return this.ballotOptions;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "A text identifying the voter that is sent to the Voting server along with the vote.  This must be set before <code>SendBallot</code> is called.")
    public String UserId() {
        return this.userId;
    }

    @SimpleProperty
    public void UserId(String userId2) {
        this.userId = userId2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The ballot choice to send to the server, which must be set before <code>SendBallot</code> is called.  This must be one of <code>BallotOptions</code>.")
    public String UserChoice() {
        return this.userChoice;
    }

    @SimpleProperty
    public void UserChoice(String userChoice2) {
        this.userChoice = userChoice2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The email address associated with this device. This property has been deprecated and always returns the empty text value.")
    public String UserEmailAddress() {
        return "";
    }

    @SimpleFunction(description = "Send a request for a ballot to the Web service specified by the property <code>ServiceURL</code>.  When the completes, one of the following events will be raised: <code>GotBallot</code>, <code>NoOpenPoll</code>, or <code>WebServiceError</code>.")
    public void RequestBallot() {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                Voting.this.postRequestBallot();
            }
        });
    }

    /* access modifiers changed from: private */
    public void postRequestBallot() {
        WebServiceUtil.getInstance().postCommandReturningObject(this.serviceURL, REQUESTBALLOT_COMMAND, (List<NameValuePair>) null, new AsyncCallbackPair<JSONObject>() {
            public void onSuccess(JSONObject result) {
                if (result == null) {
                    Voting.this.androidUIHandler.post(new Runnable() {
                        public void run() {
                            Voting.this.WebServiceError("The Web server did not respond to your request for a ballot");
                        }
                    });
                    return;
                }
                try {
                    Log.i(Voting.LOG_TAG, "postRequestBallot: ballot retrieved " + result);
                    Boolean unused = Voting.this.isPolling = Boolean.valueOf(result.getBoolean(Voting.IS_POLLING_PARAMETER));
                    if (Voting.this.isPolling.booleanValue()) {
                        Boolean unused2 = Voting.this.idRequested = Boolean.valueOf(result.getBoolean(Voting.ID_REQUESTED_PARAMETER));
                        String unused3 = Voting.this.ballotQuestion = result.getString(Voting.BALLOT_QUESTION_PARAMETER);
                        String unused4 = Voting.this.ballotOptionsString = result.getString(Voting.BALLOT_OPTIONS_PARAMETER);
                        ArrayList unused5 = Voting.this.ballotOptions = Voting.this.JSONArrayToArrayList(new JSONArray(Voting.this.ballotOptionsString));
                        Voting.this.androidUIHandler.post(new Runnable() {
                            public void run() {
                                Voting.this.GotBallot();
                            }
                        });
                        return;
                    }
                    Voting.this.androidUIHandler.post(new Runnable() {
                        public void run() {
                            Voting.this.NoOpenPoll();
                        }
                    });
                } catch (JSONException e) {
                    Voting.this.androidUIHandler.post(new Runnable() {
                        public void run() {
                            Voting.this.WebServiceError("The Web server returned a garbled object");
                        }
                    });
                }
            }

            public void onFailure(final String message) {
                Log.w(Voting.LOG_TAG, "postRequestBallot Failure " + message);
                Voting.this.androidUIHandler.post(new Runnable() {
                    public void run() {
                        Voting.this.WebServiceError(message);
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public ArrayList<String> JSONArrayToArrayList(JSONArray ja) throws JSONException {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            a.add(ja.getString(i));
        }
        return a;
    }

    @SimpleEvent(description = "Event indicating that a ballot was retrieved from the Web service and that the properties <code>BallotQuestion</code> and <code>BallotOptions</code> have been set.  This is always preceded by a call to the method <code>RequestBallot</code>.")
    public void GotBallot() {
        EventDispatcher.dispatchEvent(this, "GotBallot", new Object[0]);
    }

    @SimpleEvent
    public void NoOpenPoll() {
        EventDispatcher.dispatchEvent(this, "NoOpenPoll", new Object[0]);
    }

    @SimpleFunction(description = "Send a completed ballot to the Web service.  This should not be called until the properties <code>UserId</code> and <code>UserChoice</code> have been set by the application.")
    public void SendBallot() {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                Voting.this.postSendBallot(Voting.this.userChoice, Voting.this.userId);
            }
        });
    }

    /* access modifiers changed from: private */
    public void postSendBallot(String userChoice2, String userId2) {
        AsyncCallbackPair<String> myCallback = new AsyncCallbackPair<String>() {
            public void onSuccess(String response) {
                Voting.this.androidUIHandler.post(new Runnable() {
                    public void run() {
                        Voting.this.GotBallotConfirmation();
                    }
                });
            }

            public void onFailure(final String message) {
                Log.w(Voting.LOG_TAG, "postSendBallot Failure " + message);
                Voting.this.androidUIHandler.post(new Runnable() {
                    public void run() {
                        Voting.this.WebServiceError(message);
                    }
                });
            }
        };
        WebServiceUtil.getInstance().postCommand(this.serviceURL, SENDBALLOT_COMMAND, Lists.newArrayList(new BasicNameValuePair(USER_CHOICE_PARAMETER, userChoice2), new BasicNameValuePair(USER_ID_PARAMETER, userId2)), myCallback);
    }

    @SimpleEvent
    public void GotBallotConfirmation() {
        EventDispatcher.dispatchEvent(this, "GotBallotConfirmation", new Object[0]);
    }

    @SimpleEvent
    public void WebServiceError(String message) {
        EventDispatcher.dispatchEvent(this, "WebServiceError", message);
    }
}
