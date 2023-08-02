/*
 * MIT License
 *
 * Copyright (c) 2022-2023 MinelesNetwork
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mineles.library.component;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class LocationComponent implements Cloneable {
    public static final net.mineles.library.serializer.Serializer<LocationComponent, String> SERIALIZER = new Serializer();

    private final @NotNull String worldName;

    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    private LocationComponent(@NotNull String worldName,
                              double x, double y, double z,
                              float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @NotNull
    public static LocationComponent of(@NotNull String worldName,
                                       double x, double y, double z,
                                       float yaw, float pitch) {
        return new LocationComponent(worldName, x, y, z, yaw, pitch);
    }

    @NotNull
    public static LocationComponent from(@NotNull Location location) {
        return new LocationComponent(location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());
    }

    public @NotNull Location getAsBukkitLocation() {
        return new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public @NotNull Vector getAsBukkitVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public @NotNull String getWorldName() {
        return this.worldName;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockX() {
        return (int) Math.floor(this.x);
    }

    public int getBlockY() {
        return (int) Math.floor(this.y);
    }

    public int getBlockZ() {
        return (int) Math.floor(this.z);
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add(@NotNull LocationComponent location) {
        this.x += location.x;
        this.y += location.y;
        this.z += location.z;
    }

    public void add(@NotNull Location location) {
        this.x += location.getX();
        this.y += location.getY();
        this.z += location.getZ();
    }

    public void add(@NotNull Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();
    }

    public void subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void subtract(@NotNull LocationComponent location) {
        this.x -= location.x;
        this.y -= location.y;
        this.z -= location.z;
    }

    public void subtract(@NotNull Location location) {
        this.x -= location.getX();
        this.y -= location.getY();
        this.z -= location.getZ();
    }

    public void subtract(@NotNull Vector vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
        this.z -= vector.getZ();
    }

    public void multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    public void multiply(@NotNull LocationComponent location) {
        this.x *= location.x;
        this.y *= location.y;
        this.z *= location.z;
    }

    public void multiply(@NotNull Location location) {
        this.x *= location.getX();
        this.y *= location.getY();
        this.z *= location.getZ();
    }

    public void multiply(@NotNull Vector vector) {
        this.x *= vector.getX();
        this.y *= vector.getY();
        this.z *= vector.getZ();
    }

    public double distance(@NotNull Location location) {
        return Math.sqrt(Math.pow(this.x - location.getX(), 2) +
                Math.pow(this.y - location.getY(), 2) +
                Math.pow(this.z - location.getZ(), 2));
    }

    public double distance(@NotNull LocationComponent other) {
        return Math.sqrt(NumberConversions.square(x - other.x)
                + NumberConversions.square(y - other.y)
                + NumberConversions.square(z - other.z));
    }

    public double distanceOfYaw(@NotNull LocationComponent other) {
        return Math.sqrt(NumberConversions.square(yaw - other.yaw));
    }

    public double distanceOfPitch(@NotNull LocationComponent other) {
        return Math.sqrt(NumberConversions.square(pitch - other.pitch));
    }

    public boolean isSimilar(@NotNull LocationComponent other) {
        return this.worldName.equals(other.worldName) &&
                this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public boolean isSimilarForce(@NotNull LocationComponent other) {
        return this.worldName.equals(other.worldName) &&
                this.x == other.x && this.y == other.y && this.z == other.z &&
                this.yaw == other.yaw && this.pitch == other.pitch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        LocationComponent other = (LocationComponent) obj;
        return this.worldName.equals(other.worldName) &&
                this.x == other.x && this.y == other.y && this.z == other.z &&
                this.yaw == other.yaw && this.pitch == other.pitch;
    }

    public @NotNull String toString() {
        return SERIALIZER.serialize(this);
    }

    public @NotNull String toStringFormatted() {
        return String.format("%s, %s, %s",
                String.format("%.2f", this.x),
                String.format("%.2f", this.y),
                String.format("%.2f", this.z));
    }

    public @NotNull String toStringFormattedDirection() {
        return String.format("%s, %s",
                String.format("%.2f", this.yaw),
                String.format("%.2f", this.pitch));
    }

    @Override
    public LocationComponent clone() {
        return of(this.worldName,
                this.x, this.y, this.z,
                this.yaw, this.pitch);
    }

    private static class Serializer implements net.mineles.library.serializer.Serializer<LocationComponent, String> {
        @Override
        public @NotNull String serialize(@NotNull LocationComponent locationComponent) {
            return locationComponent.getWorldName() + ";" +
                    locationComponent.getX() + ";" + locationComponent.getY() + ";" + locationComponent.getZ() + ";" +
                    locationComponent.getYaw() + ";" + locationComponent.getPitch();
        }

        @Override
        public @NotNull LocationComponent deserialize(@NotNull String serialized) {
            String[] split = serialized.split(";");

            return LocationComponent.of(split[0],
                    Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
    }
}
