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
import de.craftinc.gates.GateChangeListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class Plugin extends JavaPlugin implements GateChangeListener
{
    protected static Plugin instance;

    protected DynmapAPI dynmapAPI;
    protected MarkerAPI markerAPI;
    protected de.craftinc.gates.Plugin gatesPlugin;
    protected GateMarkerUtil markerUtil;


    public Plugin()
    {
        instance = this;
    }


    public static Plugin getPlugin()
    {
        return instance;
    }


    @Override
    public void onEnable()
    {
        super.onEnable();

        if (!loadAPI()) {
            return;
        }

        this.markerUtil = new GateMarkerUtil(this.markerAPI);
        loadAllGateMarkers();
        this.gatesPlugin.getGatesManager().addGateChangeListener(this);
    }


    @Override
    public void onDisable()
    {
        this.gatesPlugin.getGatesManager().removeGateChangeListener(this);
        super.onDisable();
    }


    private boolean loadAPI()
    {
        PluginManager pm = getServer().getPluginManager();
        org.bukkit.plugin.Plugin dynmap = pm.getPlugin("dynmap");

        if (dynmap == null) {
            Logger.log(Level.SEVERE, "Cannot find dynmap!");
            return false;
        }

        dynmapAPI = (DynmapAPI)dynmap;
        markerAPI = dynmapAPI.getMarkerAPI();

        if (markerAPI == null) {
            Logger.log(Level.SEVERE, "Error loading Dynmap marker API!");
            return false;
        }

        gatesPlugin = (de.craftinc.gates.Plugin)pm.getPlugin("Craft Inc. Gates");

        if (gatesPlugin == null) {
            Logger.log(Level.SEVERE, "Cannot find Craft Inc. Gates");
            return false;
        }

        return true;
    }


    private void loadAllGateMarkers()
    {
        List<Gate> allGates = this.gatesPlugin.getGatesManager().allGates();

        for (Gate g : allGates) {
            this.markerUtil.addMarker(g);
        }
    }


    @Override
    public void gateChangedHandler(final Gate gate, final Map<String, Object> stringObjectMap)
    {
        if (stringObjectMap.containsKey(GateChangeListener.removedGate)) {
            this.markerUtil.removeMarker(gate);
        }
        else {
            String oldID = (String)stringObjectMap.get(GateChangeListener.changedID);
            this.markerUtil.updateMarker(gate, oldID);
        }
    }
}
