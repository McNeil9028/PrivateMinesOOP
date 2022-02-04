/*
MIT License

Copyright (c) 2021 - 2022 Kyle Hicks

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package me.untouchedodin0.plugin.util.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.untouchedodin0.plugin.PrivateMines;
import me.untouchedodin0.plugin.mines.Mine;
import me.untouchedodin0.plugin.storage.MineStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PrivateMinesExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "UntouchedOdin0";
    }

    @Override
    public @NotNull String getName() {
        return "PrivateMines";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "PrivateMines";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(final OfflinePlayer offlinePlayer, @NotNull String identifier) {
        Player player = offlinePlayer.getPlayer();
        if (player == null) {
            return null;
        }
        UUID uuid = player.getUniqueId();

        PrivateMines privateMines = PrivateMines.getPrivateMines();
        MineStorage mineStorage = privateMines.getMineStorage();
        if (identifier.equals("hasMine")) {
            return String.valueOf(mineStorage.hasWorldEditMine(uuid));
        }

        if (mineStorage.hasWorldEditMine(uuid)) {
            Mine mine = mineStorage.getWorldEditMine(uuid);
            if (identifier.equals("type")) {
                return mine.getMineType().getName();
            }
        }
        return null;
    }
}
