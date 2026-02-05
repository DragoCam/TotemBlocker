package net.nightzy.totemblocker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GithubUpdateChecker {

    private final JavaPlugin plugin;
    private final String repo;

    public GithubUpdateChecker(JavaPlugin plugin, String repo) {
        this.plugin = plugin;
        this.repo = repo;
    }


    public void check() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://api.github.com/repos/" + repo + "/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", plugin.getName());

                JsonObject json = JsonParser
                        .parseReader(new InputStreamReader(connection.getInputStream()))
                        .getAsJsonObject();

                String latestTag = json.get("tag_name").getAsString();
                String currentVersion = plugin.getDescription().getVersion();

                if (isOutdated(currentVersion, latestTag)) {
                    plugin.getLogger().warning("======================================");
                    plugin.getLogger().warning("New version available!");
                    plugin.getLogger().warning("Current: " + currentVersion);
                    plugin.getLogger().warning("Latest: " + latestTag);
                    plugin.getLogger().warning("https://github.com/" + repo + "/releases/latest");
                    plugin.getLogger().warning("======================================");
                } else {
                    plugin.getLogger().info("Plugin is up to date.");
                }

            } catch (Exception e) {
                plugin.getLogger().warning("Could not check for updates (GitHub API).");
            }
        });
    }

    private boolean isOutdated(String current, String latest) {
        String[] curParts = current.split("\\.");
        String[] latParts = latest.split("\\.");

        for (int i = 0; i < Math.max(curParts.length, latParts.length); i++) {
            int curNum = i < curParts.length ? parseIntSafe(curParts[i]) : 0;
            int latNum = i < latParts.length ? parseIntSafe(latParts[i]) : 0;

            if (curNum < latNum) return true;
            if (curNum > latNum) return false;
        }
        return false;
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
