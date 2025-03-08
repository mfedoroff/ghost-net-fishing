package com.ghostnet.util;

/**
 * Enum für die verschiedenen Status eines Geisternetzes.
 * Dieses Enum definiert die möglichen Zustände, in denen sich ein Geisternetz befinden kann.
 * Der Status bestimmt, ob ein Netz bereits gemeldet, zur Bergung vorgesehen oder bereits geborgen ist.
 */
public enum GhostNetStatus {
    REPORTED,           // Gemeldet
    RESCUE_PENDING,     // Bergung bevorstehend
    RESCUED,            // Geborgen
    LOST                // Verschollen
}
