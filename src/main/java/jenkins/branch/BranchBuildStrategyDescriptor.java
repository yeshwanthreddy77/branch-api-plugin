/*
 * The MIT License
 *
 * Copyright (c) 2017, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jenkins.branch;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.ExtensionList;
import hudson.model.Descriptor;
import java.util.ArrayList;
import java.util.List;
import jenkins.scm.api.SCMSourceDescriptor;

/**
 * {@link Descriptor} for {@link BranchBuildStrategy} instances.
 *
 * @since 2.0.0
 */
public abstract class BranchBuildStrategyDescriptor extends Descriptor<BranchBuildStrategy> {
    /**
     * A branch build strategy may not be appropriate for every type of source, this method lets a strategy
     * opt out of being selectable for a specific source type. When this method is called (via stapler) we do not have
     * an instance of the source so this needs to be hooked
     *
     * @param sourceDescriptor the source descriptor.
     * @return {@code} true iff this property strategy is relevant with this source.
     */
    public boolean isApplicable(@NonNull SCMSourceDescriptor sourceDescriptor) {
        return true;
    }

    /**
     * A branch build strategy may not be appropriate for every project, this method lets a strategy
     * opt out of being selectable for a specific project.
     * <p>By default it checks {@link #isApplicable(MultiBranchProjectDescriptor)}.
     *
     * @param project the project.
     * @return {@code} true iff this property strategy is relevant with this project instance.
     */
    public boolean isApplicable(@NonNull MultiBranchProject project) {
        return isApplicable(project.getDescriptor());
    }

    /**
     * Usually a branch property strategy is more concerned with the specific type of project than the specifics of
     * the project instance.
     *
     * @param projectDescriptor the project type.
     * @return {@code} true iff this property strategy is relevant with this project type.
     */
    protected boolean isApplicable(@NonNull MultiBranchProjectDescriptor projectDescriptor) {
        return true;
    }

    /**
     * Gets all the {@link BranchBuildStrategyDescriptor} instances.
     *
     * @return all the {@link BranchBuildStrategyDescriptor} instances.
     */
    public static List<BranchBuildStrategyDescriptor> all() {
        return ExtensionList.lookup(BranchBuildStrategyDescriptor.class);
    }

    /**
     * Gets all the {@link BranchBuildStrategyDescriptor} instances applicable to the specified project and source.
     *
     * @param project          the project
     * @param sourceDescriptor the source.
     * @return all the {@link BranchBuildStrategyDescriptor} instances  applicable to the specified project and
     * source.
     */
    public static List<BranchBuildStrategyDescriptor> all(
            @NonNull MultiBranchProject project, @NonNull SCMSourceDescriptor sourceDescriptor) {
        List<BranchBuildStrategyDescriptor> result = new ArrayList<BranchBuildStrategyDescriptor>();
        for (BranchBuildStrategyDescriptor d : all()) {
            if (d.isApplicable(project) && d.isApplicable(sourceDescriptor)) {
                result.add(d);
            }
        }
        return result;
    }

}
