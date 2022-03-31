/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.module;


import net.shadow.client.feature.module.impl.combat.*;
import net.shadow.client.feature.module.impl.crash.*;
import net.shadow.client.feature.module.impl.exploit.*;
import net.shadow.client.feature.module.impl.grief.*;
import net.shadow.client.feature.module.impl.misc.*;
import net.shadow.client.feature.module.impl.movement.*;
import net.shadow.client.feature.module.impl.render.*;
import net.shadow.client.feature.module.impl.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleRegistry {
    static final List<Module> modules = new ArrayList<>();
    static final AtomicBoolean initialized = new AtomicBoolean(false);

    public static void init() {
        if (initialized.get()) return;
        initialized.set(true);
        modules.clear();

        modules.add(new Flight());
        modules.add(new Sprint());
        modules.add(new Fullbright());
        modules.add(new Hud());
        modules.add(new TargetHud());
        //modules.add(new AntiOffhandCrash()); this should be under anticrash
        modules.add(new AntiPacketKick());
        modules.add(new AntiReducedDebugInfo());
        modules.add(new BoatPhase());
        modules.add(new Boaty());
        modules.add(new Boom());
        modules.add(new CaveMapper()); // its fun
        modules.add(new InstaBow());
        modules.add(new NoComCrash());
        modules.add(new OffhandCrash());
        modules.add(new OOBCrash());
        modules.add(new Phase());
        modules.add(new VanillaSpoof());
        modules.add(new XRAY());
        modules.add(new Decimator());
        modules.add(new ClickGUI());
        modules.add(new TpRange());
        modules.add(new AnyPlacer());
        modules.add(new FireballDeflector()); // its a fucking utility client saturn
        modules.add(new ShulkerDeflector());
        modules.add(new CarpetBomb());
        //modules.add(new SkinChangeExploit()); litteral fucking joke module, to be re-written as personhider or whatever i named it (skinfuscator is a good name lol)
        modules.add(new AutoTrap());
        modules.add(new AutoTnt());
        //modules.add(new LetThereBeLight()); awful why?
        modules.add(new FakeHacker());
        modules.add(new NoFall());
        modules.add(new ESP());
        modules.add(new Tracers());
        modules.add(new Hyperspeed());
        modules.add(new AntiAnvil());
        modules.add(new Swing());
        modules.add(new AimAssist());
        modules.add(new Criticals());
        modules.add(new Killaura()); //TODO: add settings and shit
        modules.add(new Velocity());
        modules.add(new AntiAntiXray());
        modules.add(new PingSpoof());
        modules.add(new AutoAttack());
        modules.add(new MouseEars()); //i really wanna remove this one | dont
        modules.add(new Spinner());
        modules.add(new AllowFormatCodes());
        modules.add(new InfChatLength());
        modules.add(new NoTitles());
        modules.add(new PortalGUI());
        modules.add(new Timer());
        modules.add(new XCarry());
        modules.add(new AirJump()); //TODO: unshit
        modules.add(new AutoElytra());
        modules.add(new Blink());
        modules.add(new Boost());
        modules.add(new EdgeJump()); // UTILITY CLIENT
        modules.add(new EdgeSneak());
        modules.add(new EntityFly());
        modules.add(new IgnoreWorldBorder()); //i'll allow it | as you should
        modules.add(new InventoryWalk());
        modules.add(new Jesus());
        modules.add(new LongJump());
        modules.add(new MoonGravity());
        modules.add(new NoJumpCooldown());
        modules.add(new NoLevitation());
        modules.add(new NoPush());
        modules.add(new Step());
        modules.add(new Freecam());
        modules.add(new FreeLook());
        modules.add(new ItemByteSize()); // TO BE RE-WRITTEN AS TOOLTIPS | keep it in for now tho
        modules.add(new Zoom());
        modules.add(new AutoTool()); // WHY????? this is so useless | how?
        modules.add(new BlockTagViewer());
        modules.add(new Annhilator());
        modules.add(new FastUse());
        modules.add(new Flattener());
        modules.add(new GodBridge()); //TODO: add this as a mode to scaffold
        modules.add(new InstantBreak()); //TODO: unshit
        modules.add(new MassUse());
        modules.add(new NoBreakDelay());
        modules.add(new SurvivalNuker());
        modules.add(new Nuker());
        modules.add(new Scaffold());
        modules.add(new Test());
        modules.add(new BlocksmcFlight());
        modules.add(new NameTags());
        modules.add(new Trail());
        modules.add(new MinehutAdBlocker());
        modules.add(new AutoLavacast());
        modules.add(new Backtrack());
        modules.add(new TabGui());
        modules.add(new Theme());
        modules.add(new AntiCrash());
        modules.add(new ClientSettings());
        modules.add(new NoLiquidFog());
        modules.add(new Spotlight());
        modules.add(new ShowTntPrime());
        modules.add(new ShadowScreen());
        modules.add(new BookInflator());
        modules.add(new BlockHighlighting());
        modules.add(new AutoIgnite());
        modules.add(new DiscordRPC());
        modules.add(new AirPlace());
        modules.add(new AdSpammer());
        modules.add(new AnimationCrash());
        modules.add(new AutoFireball());
        modules.add(new AutoFish());
        modules.add(new AutoRun());
    }

    public static List<Module> getModules() {
        if (!initialized.get()) {
            init();
        }
        return modules;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getByClass(Class<T> clazz) {
        if (!initialized.get()) {
            init();
        }
        for (Module module : getModules()) {
            if (module.getClass() == clazz) {
                return (T) module;
            }
        }
        throw new IllegalStateException("Unregistered module: " + clazz.getName());
    }

    public static Module getByName(String n) {
        if (!initialized.get()) {
            init();
        }
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(n)) {
                return module;
            }
        }
        return null;
    }
}
