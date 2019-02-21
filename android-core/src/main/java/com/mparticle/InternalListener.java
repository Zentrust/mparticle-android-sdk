package com.mparticle;

import android.content.ContentValues;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.mparticle.identity.MParticleUser;
import com.mparticle.internal.InternalSession;
import com.mparticle.networking.MPConnection;

import org.json.JSONObject;

public abstract class InternalListener {
    private static InternalListener implementation;

    static synchronized InternalListener getInstance() {
        if (implementation != null) {
            return implementation;
        }
        return empty;
    }

    static void setListener(InternalListener internalListener) {
        if (internalListener != null) {
            implementation = internalListener;
        }
    }

    /**
     * This method should be called within the body of a public API method. Generally
     * we only want to instrument API methods which "do something", i.e log an event or change
     * a User's state, not simple getters
     *
     * @param objects the arguments passed into the API method
     */
    public abstract void onApiCalled(Object... objects);

    /**
     * to be called when a Kit's API method is invoked. This overloaded varient should be used when
     * the name of the method containing this method's invokation (in KitManagerImpl) matches the name of the
     * Kit's method being invoked
     * @param kitId the Id of the kit
     * @param used whether the Kit's method returned ReportingMessages, or null if return type is void
     * @param objects the arguments supplied to the Kit
     */
    public abstract void onKitApiCalled(int kitId, Boolean used, Object... objects);

    /**
     * to be called when a Kit's API method is invoked, and the name of the Kit's method is different
     * than the method containing this method's invokation
     * @param methodName the name of the Kit's method being called
     * @param kitId the Id of the kit
     * @param used whether the Kit's method returned ReportingMessages, or null if return type is void
     * @param objects the arguments supplied to the Kit
     */
    public abstract void onKitApiCalled(String methodName, int kitId, Boolean used, Object... objects);

    /**
     * establishes a child-parent relationship between two objects. It is not neccessary to call this
     * method for objects headed to the MessageHandler from the public API, or objects headed to kits
     * @param child the child object
     * @param parent the parent object
     */
    public abstract void compositeObject(Object child, Object parent);

    /**
     * denotes that an object is going to be passed to the MessageHandler
     *
     * @param msg the Handler Message
     */
    public abstract void registerHandlerMessage(String handlerName, Message msg, Boolean onNewThread);

    /**
     * indicates that an entry has been stored in the Database
     * @param rowId the rowId denoted by the "_id" column value
     * @param tableName the name of the database table
     * @param contentValues the ContentValues object to be inserted
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public abstract void onMessageStored(Long rowId, String tableName, ContentValues contentValues);

    /**
     * indicates that a Network Request has been started
     * @param type the type of Request, should either be "Config", "Events" or "Identity.Login/Logout/Identify/Modify"
     * @param url the request url
     * @param body the request body
     * @param objects any underlying objects that the request body is derived from, for example, an IdentityApiRequest instance
     */
    public abstract void onNetworkRequestStarted(String type, String url, JSONObject body, Object... objects);

    /**
     * indicates that a NetworkRequest has been finished
     * @param url the request url
     * @param response the response body
     * @param responseCode the response code
     */
    public abstract void onNetworkRequestFinished(String url, JSONObject response, int responseCode);

    /**
     * this should be called the current user changes
     * @param user
     */
    public abstract void onUserIdentified(MParticleUser user);

    /**
     * this should be called when the current Session changes, for example, it starts, stops or the
     * event count changes
     * @param internalSession
     */
    public abstract void onSessionUpdated(InternalSession internalSession);

    /**
     * indicates that a Kit dependency is present
     * @param kitId
     */
    public abstract void kitFound(int kitId);

    /**
     * indicates that we have recieved a configuration for a Kit
     * @param kitId
     * @param configuration
     */
    public abstract void kitConfigReceived(int kitId, String configuration);

    /**
     * indicates that a Kit was present, and a configuration was received for it, but it was not started,
     * or it was stopped. This could be because it crashed, or becuase a User's logged in status required
     * that we shut it down
     * @param kitId
     * @param reason
     */
    public abstract void kitExcluded(int kitId, String reason);

    /**
     * indicates that a Kit successfully executed it's onKitCreate() method
     * @param kitId
     */
    public abstract void kitStarted(int kitId);

    static final InternalListener empty = new InternalListener() {
        public void onApiCalled(Object... objects) { }
        public void onKitApiCalled(int kitId, Boolean used, Object... objects) { }
        public void onKitApiCalled(String methodName, int kitId, Boolean used, Object... objects) { }
        public void compositeObject(Object child, Object parent) { }
        public void registerHandlerMessage(String handlerName, Message msg, Boolean onNewThread) { }
        public void onMessageStored(Long rowId, String tableName, ContentValues contentValues) { }
        public void onNetworkRequestStarted(String type, String url, JSONObject body, Object... objects) { }
        public void onNetworkRequestFinished(String url, JSONObject response, int responseCode) { }
        public void onUserIdentified(MParticleUser user) { }
        public void onSessionUpdated(InternalSession internalSession) { }
        public void kitFound(int kitId) { }
        public void kitConfigReceived(int kitId, String configuration) { }
        public void kitExcluded(int kitId, String reason) { }
        public void kitStarted(int kitId) { }
    };

}
