package net.kihar.anticheat.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RotationUpdate {
    private final float fromYaw, toYaw, fromPitch, toPitch;
}
