package io.github.mac_genius.npcmail.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import io.github.mac_genius.npcmail.NPCMail;

/**
 * Created by Mac on 3/27/2016.
 */
public class CustProfileCallback implements ProfileLookupCallback {
    private GameProfile profile;
    private boolean fetchWorked;

    @Override
    public void onProfileLookupSucceeded(GameProfile gameProfile) {
        NPCMail.getSingleton().getLogger().info("Lookup successful for " + gameProfile.getName() + "!");
        profile = gameProfile;
        fetchWorked = true;
    }

    @Override
    public void onProfileLookupFailed(GameProfile gameProfile, Exception e) {
        NPCMail.getSingleton().getLogger().warning("Could not lookup the profile for " + gameProfile.getName() + "!");
        fetchWorked = false;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public boolean didFetchWorked() {
        return fetchWorked;
    }
}
