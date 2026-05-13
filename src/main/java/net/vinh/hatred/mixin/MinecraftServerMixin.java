package net.vinh.hatred.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WinNativeModuleUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.vinh.hatred.api.client.screen.HudTextManager;
import net.vinh.hatred.client.camera.ScreenshakeController;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.HatredServerState;
import net.vinh.hatred.internal.util.ServerCrashHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements DataHolderInternal {
    @Unique
    private HatredServerState hatred$state;

    @Override
    public DataContainer hatred$getContainer() {
        if (hatred$state == null) {
            MinecraftServer server = (MinecraftServer)(Object)this;

            hatred$state = server.getOverworld()
                    .getPersistentStateManager()
                    .getOrCreate(
                            HatredServerState::fromNbt,
                            HatredServerState::new,
                            "hatred_server_data"
                    );
        }

        return hatred$state.getContainer();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hatred$tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer)(Object) this;

        HudTextManager.tick(server);

        if(ServerCrashHandler.shouldCrash) {
            CrashReport crashReport = new CrashReport(ServerCrashHandler.reason, new Throwable(ServerCrashHandler.reason));
            CrashReportSection crashReportSection = crashReport.addElement("Crash details");
            WinNativeModuleUtil.addDetailTo(crashReportSection);
            throw new CrashException(crashReport);
        }
    }
}
