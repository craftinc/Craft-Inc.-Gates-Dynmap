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


import java.util.logging.Level;


public class Logger
{
    public static void log(final String msg)
    {
        log(Level.INFO, msg);
    }


    public static void log(final Level level, final String msg)
    {
        java.util.logging.Logger.getLogger("Minecraft").log(level, "["+Plugin.getPlugin().getDescription().getFullName()+"] "+msg);
    }

}
