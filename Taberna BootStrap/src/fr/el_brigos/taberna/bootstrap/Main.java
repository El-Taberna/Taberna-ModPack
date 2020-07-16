package fr.el_brigos.taberna.bootstrap;

import java.io.File;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.SUpdate;

class Main {

	private static final String names = "Taberna 6";
	private static final String ServerUpdate = "http://51.178.182.151/bootstrap";
	private static final boolean setRewriteEnabled = true;

	private static final File TA_B_DIR = new File(GameDirGenerator.createGameDir(names), "Launcher");
	private static final CrashReporter TA_B_CRASH = new CrashReporter(names + " Bootstrap", TA_B_DIR);


	public static void main(String[] args) {
		try {
			doUpdate();
		} catch (Exception e) {
			TA_B_CRASH.catchError(e, "Impossible de mettre a jour le launcher !");
		}

		try {
			launch();
		} catch (Exception e) {
			TA_B_CRASH.catchError(e, "Impossible de lancer le launcher !");
		}
	}

	private static void doUpdate() throws Exception {
		SUpdate su = new SUpdate(ServerUpdate, TA_B_DIR);
		su.getServerRequester().setRewriteEnabled(setRewriteEnabled);
		su.start();
	}

	private static void launch() throws LaunchException {

		ClasspathConstructor constructor = new ClasspathConstructor();
		ExploredDirectory gameDir = Explorer.dir(TA_B_DIR);
		constructor.add(gameDir.sub("libs").allRecursive().files().match("^(.*\\.((jar)$))*$"));
		constructor.add(gameDir.get("launcher.jar"));

		ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.el_brigos.taberna.launcher.LauncherFrame", constructor.make());
		ExternalLauncher launcher = new ExternalLauncher(profile);

		Process p = launcher.launch();
		try { p.waitFor(); }
		catch (InterruptedException ignored) {}
		System.exit(0);
	}
}
