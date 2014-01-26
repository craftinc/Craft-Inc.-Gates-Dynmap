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
import org.bukkit.Location;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.io.InputStream;
import java.util.logging.Level;


public class GateMarkerUtil
{
    protected static final String markerIconID = "de.craftinc.gates.maker-icon";
    protected static final String markerIconLabel = "Gate";

    protected static final String markerSetID = "de.craftinc.gates";
    protected static final String markerSetName = "Gates";

    protected MarkerIcon gateIcon;
    protected MarkerSet markerSet;
    protected MarkerAPI markerAPI;

    public GateMarkerUtil(MarkerAPI markerAPI)
    {
        super();
        this.markerAPI = markerAPI;
        loadGateIcon();
        loadMarkerSet();
    }


    protected void loadGateIcon()
    {
        this.gateIcon = this.markerAPI.getMarkerIcon(markerIconID);

        if (this.gateIcon == null) {
            InputStream inputStream = this.getClass().getResourceAsStream("/gate.png");

            if (inputStream == null) {
                Logger.log(Level.SEVERE, "Cannot load gate icon (missing resource)");
                return;
            }

            this.gateIcon = this.markerAPI.createMarkerIcon(markerIconID, markerIconLabel, inputStream);

            if (this.gateIcon == null) {
                Logger.log(Level.SEVERE, "Cannot load gate icon");
            }
        }

    }


    protected void loadMarkerSet()
    {
        markerSet = markerAPI.getMarkerSet(markerSetID);

        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet(markerSetID, markerSetName, null, false);
        }
    }


    public void addMarker(Gate gate)
    {
        if (gate.getLocation() == null) {
            return;
        }

        String id = gate.getId();
        String label = gate.getId();
        Location l = gate.getLocation();
        MarkerIcon icon = this.gateIcon;

        this.markerSet.createMarker(id, label, false, l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), icon, false);
    }


    public void updateMarker(Gate gate, String oldID)
    {
        Marker m = this.markerSet.findMarker(gate.getId());

        if (oldID != null && m != null) {
            m.deleteMarker();
            m = null;
        }

        if (m == null) {
            this.addMarker(gate);
        }
        else if (gate.getLocation() == null) {
           this.removeMarker(gate);
        }
        else {
            Location l = gate.getLocation();
            m.setLocation(l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
        }
    }


    public void removeMarker(Gate gate)
    {
        Marker m = this.markerSet.findMarker(gate.getId());
        m.deleteMarker();
    }
}
