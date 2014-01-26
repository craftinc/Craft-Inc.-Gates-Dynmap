/*  Craft Inc. Gates Dynmap
    Copyright (C) 2011-2014 Craft Inc. Gates Team (see AUTHORS.txt)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program (LGPLv3).  If not, see <http://www.gnu.org/licenses/>.
*/
package de.craftinc.gates.dynmap;


import de.craftinc.gates.Gate;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Plugin extends JavaPlugin
{
    private static final String markerSetID = "de.craftinc.gates";
    private static final String markerSetName = "Gates";

    private static final String markerIconID = "de.craftinc.gates.maker-icon";
    private static final String markerIconLabel = "Gate";


    DynmapAPI dynmapAPI;
    MarkerAPI markerAPI;
    de.craftinc.gates.Plugin gatesPlugin;

    MarkerSet gateMarkers;
    MarkerIcon gateIcon;



    public void onEnable()
    {
        if (!loadAPI()) {
            return;
        }

        loadIcons();
        createMakersSet();
        loadAllGateMarkers();
        setupChangeListener();
    }


    private boolean loadAPI()
    {
        PluginManager pm = getServer().getPluginManager();
        org.bukkit.plugin.Plugin dynmap = pm.getPlugin("dynmap");

        if (dynmap == null) {
            log(Level.SEVERE, "Cannot find dynmap!");
            return false;
        }

        dynmapAPI = (DynmapAPI)dynmap;
        markerAPI = dynmapAPI.getMarkerAPI();

        if (markerAPI == null) {
            log(Level.SEVERE, "Error loading Dynmap marker API!");
            return false;
        }

        gatesPlugin = (de.craftinc.gates.Plugin)pm.getPlugin("Craft Inc. Gates");

        if (gatesPlugin == null) {
            log(Level.SEVERE, "Cannot find Craft Inc. Gates");
            return false;
        }

        return true;
    }


    private void loadIcons()
    {
        InputStream inputStream = this.getClass().getResourceAsStream("/gate.png");

        if (inputStream == null) {
            log(Level.SEVERE, "Cannot load gate icon (missing resource)");
            return;
        }

        this.gateIcon = this.markerAPI.createMarkerIcon(markerIconID, markerIconLabel, inputStream);

        if (this.gateIcon == null) {
            log(Level.SEVERE, "Cannot load gate icon");
        }
    }



    private void createMakersSet()
    {
        gateMarkers = markerAPI.getMarkerSet(markerSetID);

        if (gateMarkers == null) {
            gateMarkers = markerAPI.createMarkerSet(markerSetID, markerSetName, null, false);
        }
    }


    private void loadAllGateMarkers()
    {
        List<Gate> allGates = this.gatesPlugin.getGatesManager().allGates();

        for (Gate g : allGates) {
            String id = g.getId();
            String label = g.getId();
            boolean markup = false;
            String world = g.getLocation().getWorld().getName();
            double x = g.getLocation().getX();
            double y = g.getLocation().getY();
            double z = g.getLocation().getZ();
            MarkerIcon icon = this.gateIcon;
            boolean is_persistent = false;

            this.gateMarkers.createMarker(id, label, markup, world, x, y, z, icon, is_persistent);
        }
    }

    private void setupChangeListener()
    {
        // TODO: implement this
    }


    /*
     * Logging
     */
    public void log(String msg)
    {
        log(Level.INFO, msg);
    }


    public void log(Level level, String msg)
    {
        Logger.getLogger("Minecraft").log(level, "["+this.getDescription().getFullName()+"] "+msg);
    }

}
