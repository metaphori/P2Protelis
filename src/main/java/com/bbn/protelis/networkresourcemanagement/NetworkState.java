package com.bbn.protelis.networkresourcemanagement;

import javax.annotation.Nonnull;

/**
 * Contains the network information for a region.
 */
public class NetworkState {

    /**
     * Create an object with no known network state.
     *
     * @param regionName
     *            the region that this state is for
     */
    public NetworkState(final String regionName) {
        this.regionName = regionName;
        this.regionSummary = ResourceSummary.getNullSummary(regionName);
        this.regionPlan = RegionPlan.getNullPlan(regionName);
    }

    private final String regionName;

    /**
     * @return the region that this network state is for
     */
    @Nonnull
    public String getRegionName() {
        return regionName;
    }

    private ResourceSummary regionSummary;

    /**
     * 
     * @return the summary for the region.
     */
    @Nonnull
    public ResourceSummary getRegionSummary() {
        return this.regionSummary;
    }

    /**
     * Specify a new summary for the region.
     * 
     * @param summary
     *            the new summary
     * 
     * @throws IllegalArgumentException
     *             if the summary is for a different region than the node
     */
    public void setRegionSummary(@Nonnull final ResourceSummary summary) {
        if (!summary.getRegionName().equals(this.regionName)) {
            throw new IllegalArgumentException(
                    "Region summary must be for the same region as the network state object");
        }

        regionSummary = summary;
    }

    private RegionPlan regionPlan;

    /**
     * @return the current plan for the region
     */
    @Nonnull
    public RegionPlan getRegionPlan() {
        return regionPlan;
    }

    /**
     * 
     * @param plan
     *            the new plan for the region
     * @throws IllegalArgumentException
     *             if the plan is for a different region than the node
     */
    public void setRegionPlan(@Nonnull final RegionPlan plan) {
        if (!plan.getRegionName().equals(this.regionName)) {
            throw new IllegalArgumentException(
                    "Region summary must be for the same region as the network state object");
        }

        this.regionPlan = plan;
    }

}
