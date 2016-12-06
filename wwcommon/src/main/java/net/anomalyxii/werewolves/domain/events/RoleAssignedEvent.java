package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.Role;

import java.util.Calendar;

/**
 * Created by Anomaly on 05/12/2016.
 */
public class RoleAssignedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final Role role;

    // ******************************
    // Constructors
    // ******************************

    public RoleAssignedEvent(Player player, Calendar timestamp, Role role) {
        super(player, timestamp, EventType.ROLE_ASSIGNED);
        this.role = role;
    }

    // ******************************
    // Getters
    // ******************************

    public Role getRole() {
        return role;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] %s is a %s", getTimestamp(), getPlayer().getName(), role);
    }

}
