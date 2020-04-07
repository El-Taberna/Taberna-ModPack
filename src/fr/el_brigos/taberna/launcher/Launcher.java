package fr.el_brigos.taberna.launcher;

import java.io.File;

import fr.theshark34.openlauncherlib.launcher.GameInfos;
import fr.theshark34.openlauncherlib.launcher.GameTweak;
import fr.theshark34.openlauncherlib.launcher.GameType;
import fr.theshark34.openlauncherlib.launcher.GameVersion;

public class Launcher {

	public static final GameVersion TA_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos TA_INFOS = new GameInfos("Taberna 5", TA_VERSION, true, new GameTweak[] {GameTweak.FORGE});
	public static final File TA_DIR = TA_INFOS.getGameDir();
}
