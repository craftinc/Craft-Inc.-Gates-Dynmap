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

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
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
            final InputStream inputStream = this.getClass().getResourceAsStream("/gate.png");

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


    protected String getGateHTMLDescription(final Gate gate)
    {
        final String gateId = "<div class=\"infowindow\"><h2>";
        final String open = "</h2><p>This gate is <span style=\"font-weight:bold;\">";
        final String hidden = "</span> and <span style=\"font-weight:bold;\">hidden</span>";
        final String preExitLink = "<br/><a href=\"";
        final String afterExitLink = "\">Go to exit</a>";
        final String end = "</p></div>";

        String infoString = gateId + escapeHtml(gate.getId()) + open;

        if (gate.isOpen()) {
            infoString += "open";
        }
        else {
            infoString = "closed";
        }

        if (gate.isHidden()) {
            infoString += hidden;
        }

        if (gate.getExit() != null) {
            Location exit = gate.getExit();

            infoString += preExitLink;

            infoString += "/?worldname=" + escapeHtml(exit.getWorld().getName());
            infoString += "&zoom=10&x=" + exit.getX();
            infoString += "&y=" + exit.getY();
            infoString += "&z=" + exit.getZ();

            infoString += afterExitLink;
        }

        infoString += end;

        return infoString;
    }

    public void addMarker(final Gate gate)
    {
        if (gate.getLocation() == null) {
            return;
        }

        final String id = gate.getId();
        final String label = gate.getId();
        final Location l = gate.getLocation();
        final MarkerIcon icon = this.gateIcon;

        final Marker marker = this.markerSet.createMarker(id, label, false, l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), icon, false);
        marker.setDescription(this.getGateHTMLDescription(gate));
    }


    public void updateMarker(final Gate gate, final String oldID)
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
            final Location l = gate.getLocation();
            m.setLocation(l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
            m.setDescription(this.getGateHTMLDescription(gate));
        }
    }


    public void removeMarker(final Gate gate)
    {
        final Marker m = this.markerSet.findMarker(gate.getId());
        m.deleteMarker();
    }
}
