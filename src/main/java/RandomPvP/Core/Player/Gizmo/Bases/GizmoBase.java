package RandomPvP.Core.Player.Gizmo.Bases;

import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public abstract class GizmoBase {

    public abstract String getDisplayName();

    public abstract List<String> getDesc();

    public abstract int getPrice();

    public abstract boolean donorOnly();

}
