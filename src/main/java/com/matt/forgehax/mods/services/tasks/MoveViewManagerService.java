package com.matt.forgehax.mods.services.tasks;

import com.google.common.eventbus.Subscribe;
import com.matt.forgehax.asm.events.LocalPlayerUpdateMovementEvent;
import com.matt.forgehax.util.mod.ServiceMod;
import com.matt.forgehax.util.mod.loader.RegisterMod;
import com.matt.forgehax.util.task.Task;
import com.matt.forgehax.util.task.TaskManager;

/**
 * Created on 6/15/2017 by fr1kin
 */
@RegisterMod
public class MoveViewManagerService extends ServiceMod {
    public MoveViewManagerService() {
        super("MoveViewManagerService");
    }

    private Task.TaskProcessing processing = null;

    @Subscribe
    public void onMovementUpdatePre(LocalPlayerUpdateMovementEvent.Pre event) {
        processing = TaskManager.getTop(Task.Type.LOOK);
        if(processing != null) processing.preProcessing();
    }

    @Subscribe
    public void onMovementUpdatePost(LocalPlayerUpdateMovementEvent.Post event) {
        if(processing != null) processing.postProcessing();
    }
}
