/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022 Mike Millson                                                                                    *
 *                                                                                                                    *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License       *
 * v. 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0 which is    *
 * available at https://www.apache.org/licenses/LICENSE-2.0.                                                          *
 *                                                                                                                    *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0                                                                     *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.github.joa.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.github.joa.JvmOptions;
import org.github.joa.domain.JvmContext;
import org.junit.jupiter.api.Test;

public class TestAnalysis {

    @Test
    void testAttachMechanismDisabled() {
        String opts = "-Xss128k -XX:+DisableAttachMechanism -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DISABLE_ATTACH_MECHANISM),
                Analysis.WARN_DISABLE_ATTACH_MECHANISM + " analysis not identified.");
    }

    @Test
    void testBisasedLockingDisabledNotShenandoah() {
        String opts = "-Xss128k -XX:-UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BIASED_LOCKING_DISABLED),
                Analysis.WARN_BIASED_LOCKING_DISABLED + " analysis not identified.");
    }

    @Test
    void testBisasedLockingDisabledShenandoah() {
        String opts = "-XX:+UseShenandoahGC -Xss128k -XX:-UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_BIASED_LOCKING_DISABLED),
                Analysis.WARN_BIASED_LOCKING_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testBytecodeBackgroundCompilationDisabledNonStandard() {
        String opts = "-Xss128k -Xbatch -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_BACK_COMP_DISABLED),
                Analysis.WARN_BYTECODE_BACK_COMP_DISABLED + " analysis not identified.");
    }

    @Test
    void testBytecodeBackgroundCompilationDisabledStandard() {
        String opts = "-Xss128k -XX:-BackgroundCompilation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_BACK_COMP_DISABLED),
                Analysis.WARN_BYTECODE_BACK_COMP_DISABLED + " analysis not identified.");
    }

    @Test
    void testCGroupMemoryLimit() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT),
                Analysis.WARN_CGROUP_MEMORY_LIMIT + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE),
                Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testCGroupMemoryLimitOverrideMaxHeap() {
        String opts = "-XX:MaxHeapSize=2048m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE),
                Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT),
                Analysis.WARN_CGROUP_MEMORY_LIMIT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testCGroupMemoryLimitOverrideXmx() {
        String opts = "-Xmx2048m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE),
                Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT),
                Analysis.WARN_CGROUP_MEMORY_LIMIT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testClassUnloadingDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-ClassUnloading";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CLASS_UNLOADING_DISABLED),
                Analysis.WARN_CLASS_UNLOADING_DISABLED + " analysis not identified.");
    }

    @Test
    void testClassUnloadingDisabledCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:-CMSClassUnloadingEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED),
                Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED + " analysis not identified.");
    }

    @Test
    void testClassUnloadingDisabledCmsDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseConcMarkSweepGC -XX:-CMSClassUnloadingEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED),
                Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testClassVerificationDisabled() {
        String opts = "-Xss512 -Xmx33g -Xverify:none";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_VERIFY_NONE),
                Analysis.WARN_VERIFY_NONE + " analysis not identified.");
    }

    @Test
    void testClientFlag() {
        String opts = "-Xss512 -client -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        // 64-bit is assumed
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_64_CLIENT),
                Analysis.INFO_64_CLIENT + " analysis not identified.");
    }

    /**
     * Test if CMS collector disabled with -XX:-UseConcMarkSweepGC -XX:-UseParNewGC.
     */
    @Test
    void testCmsDisabledJdkUnknown() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParNewGC -XX:-UseConcMarkSweepGC "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED),
                Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED),
                Analysis.INFO_CMS_DISABLED + " analysis not identified.");
    }

    @Test
    void testCmsEdenChunksRecordAlways() {
        String opts = "-Xss128k -XX:+CMSEdenChunksRecordAlways -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_EDEN_CHUNK_RECORD_ALWAYS),
                Analysis.INFO_CMS_EDEN_CHUNK_RECORD_ALWAYS + " analysis not identified.");
    }

    @Test
    void testCmsIncrementalModeWithInitatingOccupancyFractionCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT),
                Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT + " analysis not identified.");

    }

    @Test
    void testCmsIncrementalModeWithInitatingOccupancyFractionCmsDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseConcMarkSweepGC -XX:+CMSIncrementalMode "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT),
                Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT + " analysis incorrectly identified.");
    }

    /**
     * Test if PAR_NEW collector is enabled/disabled when the CMS collector is not used.
     */
    @Test
    void testCmsParNewCruftJdkUnknown() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParNewGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED),
                Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis not identified.");
    }

    /**
     * Test if PAR_NEW collector is enabled/disabled when the CMS collector is not used.
     */
    @Test
    void testCmsParNewCruftNone() {
        String opts = "-Xss128k -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED),
                Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
    }

    @Test
    void testCmsWaitDuration() {
        String opts = "-Xss128k -XX:CMSWaitDuration=10000 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_WAIT_DURATION),
                Analysis.INFO_CMS_WAIT_DURATION + " analysis not identified.");
    }

    /**
     * Test analysis just in time (JIT) compiler disabled.
     */
    @Test
    void testCompilationDisabled() {
        String opts = "-Xss128k -Xint -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_COMPILE_DISABLED),
                Analysis.WARN_BYTECODE_COMPILE_DISABLED + " analysis not identified.");
    }

    /**
     * Test analysis compilation on first invocation enabled.
     */
    @Test
    void testCompilationOnFirstInvocation() {
        String opts = "-Xss128k -Xcomp -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_COMPILE_FIRST_INVOCATION),
                Analysis.WARN_BYTECODE_COMPILE_FIRST_INVOCATION + " analysis not identified.");
    }

    @Test
    void testCompilerThreads() {
        String opts = "-Xss128k -XX:CICompilerCount=2 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CI_COMPILER_COUNT),
                Analysis.INFO_CI_COMPILER_COUNT + " analysis not identified.");
    }

    @Test
    void testCompileThresholdIgnored() {
        String opts = "-Xss512 -XX:CompileThreshold=200 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_COMPILE_THRESHOLD_IGNORED),
                Analysis.INFO_COMPILE_THRESHOLD_IGNORED + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersDisabledHeapLt32G() {
        String opts = "-Xss128k -XX:-UseCompressedClassPointers -XX:+UseCompressedOops -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_DISABLED_HEAP_LT_32G),
                Analysis.WARN_COMP_CLASS_DISABLED_HEAP_LT_32G + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:-UseCompressedClassPointers -XX:+UseCompressedOops";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK),
                Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersEnabledCompressedOopsDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersEnabledHeapGt32G() {
        String opts = "-Xss128k -XX:+UseCompressedClassPointers -Xmx32g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G),
                Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G),
                Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G + " incorrectly identified.");
    }

    @Test
    void testCompressedClassSpaceSizeWithCompressedClassPointersDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:CompressedClassSpaceSize=1G -XX:-UseCompressedClassPointers -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK),
                Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_COMP_CLASS_SIZE_COMP_CLASS_DISABLED),
                Analysis.INFO_COMP_CLASS_SIZE_COMP_CLASS_DISABLED + " analysis not identified.");
    }

    @Test
    void testCompressedClassSpaceSizeWithCompressedOopsDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:CompressedClassSpaceSize=1G -XX:-UseCompressedOops -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_COMP_CLASS_SIZE_COMP_OOPS_DISABLED),
                Analysis.INFO_COMP_CLASS_SIZE_COMP_OOPS_DISABLED + " analysis not identified.");
    }

    @Test
    void testCompressedOopsDisabledHeapEqual32G() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xmx32G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK + " analysis incorrectly identified.");
    }

    @Test
    void testCompressedOopsDisabledHeapGreater32G() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xmx40G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G + " analysis incorrectly identified.");
    }

    @Test
    void testCompressedOopsDisabledHeapLess32G() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G + " analysis not identified.");
    }

    @Test
    void testCompressedOopsEnabledHeapGreater32G() {
        String opts = "-Xss128k -XX:+UseCompressedOops -Xmx40G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G),
                Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G),
                Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G + " incorrectly identified.");
    }

    @Test
    void testConcurrentio() {
        String opts = "-Xss128k -Xconcurrentio -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CONCURRENTIO),
                Analysis.WARN_CONCURRENTIO + " analysis not identified.");
    }

    @Test
    void testD64Flag() {
        String opts = "-Xss512 -d64 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        // 64-bit is assumed
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_64_D64_REDUNDANT),
                Analysis.INFO_64_D64_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testDebug() {
        String opts = "-Xdebug -XX:+PrintHeapAtGC -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DEBUG), Analysis.INFO_DEBUG + " analysis not identified.");
    }

    /**
     * Test analysis huge DGC intervals.
     */
    @Test
    void testDgcHugeIntervals() {
        String opts = "-Dsun.rmi.dgc.client.gcInteraval=9223372036854775807 "
                + "-Dsun.rmi.dgc.server.gcInterval=9223372036854775807";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE + " analysis nto identified.");
    }

    /**
     * Test analysis not small DGC intervals.
     */
    @Test
    void testDgcNotSmallIntervals() {
        String opts = "-Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis incorrectly identified.");
    }

    /**
     * Test DGC redundant options analysis.
     */
    @Test
    void testDgcRedundantOptions() {
        String opts = "-XX:+DisableExplicitGC -Dsun.rmi.dgc.client.gcInterval=14400000 "
                + "-Dsun.rmi.dgc.server.gcInterval=24400000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_RMI_DGC_CLIENT_GCINTERVAL_REDUNDANT),
                Analysis.INFO_RMI_DGC_CLIENT_GCINTERVAL_REDUNDANT + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_RMI_DGC_SERVER_GCINTERVAL_REDUNDANT),
                Analysis.INFO_RMI_DGC_SERVER_GCINTERVAL_REDUNDANT + " analysis not identified.");
    }

    /**
     * Test analysis small DGC intervals
     */
    @Test
    void testDgcSmallIntervals() {
        String opts = "-Dsun.rmi.dgc.client.gcInterval=3599999 -Dsun.rmi.dgc.server.gcInterval=3599999";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis not identified.");
    }

    /**
     * Test analysis small DGC intervals with explicit gc disable
     */
    @Test
    void testDgcSmallIntervalsDisableExplicitGc() {
        String opts = "-Dsun.rmi.dgc.client.gcInterval=3599999 -Dsun.rmi.dgc.server.gcInterval=3599999 "
                + "-XX:+DisableExplicitGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis incorrectly identified.");
    }

    /**
     * Test DisableExplicitGC in combination with ExplicitGCInvokesConcurrent.
     */
    @Test
    void testDisableExplictGc() {
        String opts = "-Xss128k -XX:+DisableExplicitGC -XX:+ExplicitGCInvokesConcurrent -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPLICIT_GC_DISABLED),
                Analysis.WARN_EXPLICIT_GC_DISABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPLICIT_GC_DISABLED_CONCURRENT),
                Analysis.WARN_EXPLICIT_GC_DISABLED_CONCURRENT + " analysis not identified.");
    }

    @Test
    void testEliminateLocks() {
        String opts = "-Xss512 -Xmx33g -XX:+EliminateLocks";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_ELIMINATE_LOCKS_ENABLED),
                Analysis.INFO_ELIMINATE_LOCKS_ENABLED + " analysis not identified.");
    }

    @Test
    void testExplicitGCInvokesConcurrentAndUnloadsClassesDisabled() {
        String opts = "-Xss128k -XX:+DisableExplicitGC -XX:-ExplicitGCInvokesConcurrentAndUnloadsClasses";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CRUFT_EXP_GC_INV_CON_AND_UNL_CLA),
                Analysis.INFO_CRUFT_EXP_GC_INV_CON_AND_UNL_CLA + " analysis not identified.");
    }

    /**
     * Test if explicit not GC handled concurrently.
     */
    @Test
    void testG1MixedGCLiveThresholdPercent() {
        String opts = "-Xss128k -Xms2048M -XX:+UseG1GC -XX:G1MixedGCLiveThresholdPercent=50";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_G1_MIXED_GC_LIVE_THRSHOLD_PRCNT),
                Analysis.WARN_G1_MIXED_GC_LIVE_THRSHOLD_PRCNT + " analysis not identified.");
    }

    @Test
    void testGcLoggingToStdout() {
        String opts = "-XX:+PrintGC -XX:+PrintGCCause -XX:+PrintGCDateStamps -XX:+PrintGCDetails "
                + "-XX:+PrintGCTimeStamps";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_NOT_ENABLED),
                Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_NOT_ENABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_GC_LOG_STDOUT),
                Analysis.INFO_GC_LOG_STDOUT + " analysis not identified.");
    }

    @Test
    void testGuaranteedSafepointInterval() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=100000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DIAGNOSTICS_GUARANTEED_SAFEPOINT_INTERVAL),
                Analysis.WARN_DIAGNOSTICS_GUARANTEED_SAFEPOINT_INTERVAL + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:-HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_ON_OOME_DISABLED),
                Analysis.WARN_HEAP_DUMP_ON_OOME_DISABLED + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorMissing() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_HEAP_DUMP_ON_OOME_MISSING),
                Analysis.INFO_HEAP_DUMP_ON_OOME_MISSING + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathIsDirectory() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError "
                + "-XX:HeapDumpPath=/path/to";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_PATH_FILENAME),
                Analysis.WARN_HEAP_DUMP_PATH_FILENAME + " analysis incorrectly identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathIsFileName() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError "
                + "-XX:HeapDumpPath=/path/to/heap.hprof";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_PATH_FILENAME),
                Analysis.WARN_HEAP_DUMP_PATH_FILENAME + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathMissing() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_HEAP_DUMP_PATH_MISSING),
                Analysis.INFO_HEAP_DUMP_PATH_MISSING + " analysis not identified.");
    }

    @Test
    void testInitatingOccupancyOnlyMissingCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING),
                Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING + " analysis not identified.");
    }

    @Test
    void testInitatingOccupancyOnlyMissingCmsDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING),
                Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING + " analysis incorrectly identified.");
    }

    /**
     * Test if explicit not GC handled concurrently.
     */
    @Test
    void testInitialNotEqualMaxHeap() {
        String opts = "-Xss128k -Xms2048M -Xmx4096M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_HEAP_MIN_NOT_EQUAL_MAX),
                Analysis.INFO_HEAP_MIN_NOT_EQUAL_MAX + " analysis not identified.");
    }

    @Test
    void testInstrumentation() {
        String opts = "-Xss128k -javaagent:/path/to/appdynamics/javaagent.jar -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_INSTRUMENTATION),
                Analysis.INFO_INSTRUMENTATION + " analysis not identified.");
    }

    @Test
    void testJdk11GcLogFileOverwrite() {
        String opts = "-Xlog:gc*,safepoint=info:file=gc.log:uptimemillis:filecount=0,filesize=50M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_GC_LOG_FILE_OVERWRITE),
                Analysis.WARN_JDK11_GC_LOG_FILE_OVERWRITE + " analysis not identified.");
    }

    @Test
    void testJdk8GcLogFileRotationDisabledOverwrite() {
        String opts = "-XX:+PrintGC -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps "
                + "-XX:+PrintGCApplicationStoppedTime -XX:-UseGCLogFileRotation";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE),
                Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE + " analysis not identified.");
    }

    @Test
    void testJdk8GcLogFileRotationNotEnabledOverwrite() {
        String opts = "-XX:+PrintGC -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps "
                + "-XX:+PrintGCApplicationStoppedTime";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE),
                Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE + " analysis not identified.");
    }

    @Test
    void testJdk8LogFileSizeSmall() {
        String opts = "-Xss128k -XX:NumberOfGCLogFiles=5 -XX:+UseGCLogFileRotation -XX:GCLogFileSize=8192";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_SIZE_SMALL),
                Analysis.WARN_JDK8_GC_LOG_FILE_SIZE_SMALL + " analysis not identified.");
    }

    @Test
    void testJdk8PrintGCDetailsMissingGcLogging() {
        String opts = "-Xss128k -Xloggc:gc.log -Xms2048M";
        JvmContext context = new JvmContext(opts, 8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_GC_DETAILS_MISSING),
                Analysis.INFO_JDK8_PRINT_GC_DETAILS_MISSING + " analysis not identified.");
    }

    @Test
    void testJdk8PrintGCDetailsMissingNoGcLogging() {
        String opts = "-Xss128k -Xms2048M";
        JvmContext context = new JvmContext(opts, 8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_GC_DETAILS_MISSING),
                Analysis.INFO_JDK8_PRINT_GC_DETAILS_MISSING + " analysis incorrectly identified.");
    }

    @Test
    void testJmxManagementServerEnabled() {
        String opts = "-Xss128k -XX:+ManagementServer -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JMX_ENABLED),
                Analysis.INFO_JMX_ENABLED + " analysis not identified.");
    }

    @Test
    void testJmxSystemProperty() {
        String opts = "-Xss128k -Dcom.sun.management.jmxremote -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JMX_ENABLED),
                Analysis.INFO_JMX_ENABLED + " analysis not identified.");
    }

    @Test
    void testLogFileNumberWithRotationDisabled() {
        String opts = "-Xss128k -XX:NumberOfGCLogFiles=5 -XX:-UseGCLogFileRotation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED),
                Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED_NUM),
                Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED_NUM + " analysis not identified.");
    }

    @Test
    void testMaxPermSize() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MAX_PERM_SIZE),
                Analysis.INFO_MAX_PERM_SIZE + " analysis not identified.");
    }

    @Test
    void testMetaspaceEq32g() {
        String opts = "-Xss512 -Xmx32g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA),
                Analysis.INFO_METASPACE_CLASS_METADATA + " analysis not identified.");
    }

    @Test
    void testMetaspaceGt32G() {
        String opts = "-Xss512 -Xmx33g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA),
                Analysis.INFO_METASPACE_CLASS_METADATA + " analysis not identified.");
    }

    @Test
    void testMetaspaceLt32g() {
        String opts = "-Xss512 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " analysis not identified.");
    }

    @Test
    void testMinHeapDeltaBytes() {
        String opts = "-Xms1g -XX:MinHeapDeltaBytes=12345 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MIN_HEAP_DELTA_BYTES),
                Analysis.INFO_MIN_HEAP_DELTA_BYTES + " analysis not identified.");
    }

    /**
     * Test analysis if native library being used.
     */
    @Test
    void testNative() {
        String opts = "-Xss128k -Xms2048M -agentpath:/path/to/agent.so -Xmx2048";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_NATIVE_AGENT),
                Analysis.INFO_NATIVE_AGENT + " analysis not identified.");
    }

    /**
     * Test analysis if new space &gt; old space.
     */
    @Test
    void testNewRatioInverted() {
        String opts = "-Xss128k -Xmx4g -XX:NewSize=2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_NEW_RATIO_INVERTED),
                Analysis.INFO_NEW_RATIO_INVERTED + " analysis not identified.");
    }

    @Test
    void testOnOutOfMemoryError() {
        String opts = "-Xss128k -XX:OnOutOfMemoryError=\"/usr/bin/restart_tomcat\" -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_ON_OOME_KILL),
                Analysis.INFO_ON_OOME_KILL + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_ON_OOME), Analysis.INFO_ON_OOME + " analysis not identified.");
    }

    @Test
    void testParalleGcThreads() {
        String opts = "-Xss128k -XX:ParallelGCThreads=4 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_GC_THREADS_1),
                Analysis.ERROR_PARALLEL_GC_THREADS_1 + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_GC_THREADS_SERIAL),
                Analysis.INFO_PARALLEL_GC_THREADS_SERIAL + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_GC_THREADS),
                Analysis.INFO_PARALLEL_GC_THREADS + " analysis not identified.");
    }

    @Test
    void testParalleGcThreads1() {
        String opts = "-Xss128k -XX:ParallelGCThreads=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_GC_THREADS_1),
                Analysis.ERROR_PARALLEL_GC_THREADS_1 + " analysis not identified.");
    }

    @Test
    void testParalleGcThreadsSerial() {
        String opts = "-Xss128k -XX:+UseSerialGC -XX:ParallelGCThreads=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_GC_THREADS_1),
                Analysis.ERROR_PARALLEL_GC_THREADS_1 + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_GC_THREADS_SERIAL),
                Analysis.INFO_PARALLEL_GC_THREADS_SERIAL + " analysis not identified.");
    }

    @Test
    void testParallelClassLoading() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:+UnsyncloadClass -Xmx2G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DIAGNOSTIC_UNSYNCLOAD_CLASS),
                Analysis.WARN_DIAGNOSTIC_UNSYNCLOAD_CLASS + " analysis not identified.");
    }

    @Test
    void testParallelInitialMarkDisabledCms() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:-CMSParallelInitialMarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED),
                Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED + " analysis not identified.");
    }

    @Test
    void testParallelInitialMarkDisabledCmsDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-UseConcMarkSweepGC -XX:-CMSParallelInitialMarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED),
                Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED + " analysis incorrectly identified.");
    }

    /**
     * Test PARALLEL_OLD collector is enabled/disabled when the parallel collector is not used.
     */
    @Test
    void testParallelOldfCruftJdkUnknown() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParallelGC -XX:+UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK11_PARALLEL_OLD_CRUFT),
                Analysis.INFO_JDK11_PARALLEL_OLD_CRUFT + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_PARALLEL_OLD_DISABLED),
                Analysis.WARN_JDK11_PARALLEL_OLD_DISABLED + " analysis incorrectly identified.");
    }

    /**
     * Test if PARALLEL_OLD collector is enabled/disabled when the parallel collector is not used false positive.
     */
    @Test
    void testParallelOldfCruftNone() {
        String opts = "-Xss128k -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK11_PARALLEL_OLD_CRUFT),
                Analysis.INFO_JDK11_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_PARALLEL_OLD_DISABLED),
                Analysis.WARN_JDK11_PARALLEL_OLD_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testParallelRemarkEnabledCms() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:-CMSParallelRemarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED),
                Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED + " analysis not identified.");
    }

    @Test
    void testParallelRemarkEnabledCmsDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-UseConcMarkSweepGC -XX:-CMSParallelRemarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED),
                Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testPerfDataDisabled() {
        String opts = "-Xss512 -XX:-UsePerfData -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PERF_DATA_DISABLED),
                Analysis.INFO_PERF_DATA_DISABLED + " analysis not identified.");
    }

    @Test
    void testPermSize() {
        String opts = "-Xms1024m -Xmx2048m -XX:PermSize=256m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PERM_SIZE),
                Analysis.INFO_PERM_SIZE + " analysis not identified.");
    }

    @Test
    void testPrintAdaptiveSizePolicyDisabled() {
        String opts = "-Xss128k -Xmx4g -XX:-PrintAdaptiveSizePolicy";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_DISABLED),
                Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_DISABLED + " analysis not identified.");
    }

    @Test
    void testPrintAdaptiveSizePolicyEnabled() {
        String opts = "-Xss128k -Xmx4g -XX:+PrintAdaptiveSizePolicy";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_ENABLED),
                Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_ENABLED + " analysis not identified.");
    }

    @Test
    void testPrintClassHistogram() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintClassHistogram";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_PRINT_CLASS_HISTOGRAM),
                Analysis.WARN_PRINT_CLASS_HISTOGRAM + " analysis not identified.");
    }

    @Test
    void testPrintClassHistogramAfterFullGc() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintClassHistogramAfterFullGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_PRINT_CLASS_HISTOGRAM_AFTER_FULL_GC),
                Analysis.WARN_PRINT_CLASS_HISTOGRAM_AFTER_FULL_GC + " analysis not identified.");
    }

    @Test
    void testPrintClassHistogramBeforeFullGc() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintClassHistogramBeforeFullGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_PRINT_CLASS_HISTOGRAM_BEFORE_FULL_GC),
                Analysis.WARN_PRINT_CLASS_HISTOGRAM_BEFORE_FULL_GC + " analysis not identified.");
    }

    @Test
    void testPrintFLSStatistics() {
        String opts = "-Xss128k -XX:PrintFLSStatistics=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_FLS_STATISTICS),
                Analysis.INFO_JDK8_PRINT_FLS_STATISTICS + " analysis not identified.");
    }

    /**
     * Test if explicit not GC handled concurrently.
     */
    @Test
    void testPrintGCApplicationConcurrentTime() {
        String opts = "-Xss128k -Xms2048M -Xmx4096M -XX:+PrintGCApplicationConcurrentTime";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PRINT_GC_APPLICATION_CONCURRENT_TIME),
                Analysis.INFO_PRINT_GC_APPLICATION_CONCURRENT_TIME + " analysis not identified.");
    }

    @Test
    void testPrintGcCause() {
        String opts = "-Xms1024m -Xmx2048m -XX:+PrintGCCause -XX:-HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_GC_CAUSE),
                Analysis.INFO_JDK8_PRINT_GC_CAUSE + " analysis not identified.");
    }

    @Test
    void testPrintGcCauseDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-PrintGCCause -XX:-HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_CAUSE_DISABLED),
                Analysis.WARN_JDK8_PRINT_GC_CAUSE_DISABLED + " analysis not identified.");
    }

    /**
     * Test with PrintGCDetails disabled with -XX:-PrintGCDetails.
     */
    @Test
    void testPrintGCDetailsDisabled() {
        String opts = "-Xss128k -XX:-PrintGCDetails -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_DISABLED),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_DISABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_GC_DETAILS_MISSING),
                Analysis.INFO_JDK8_PRINT_GC_DETAILS_MISSING + " analysis incorrectly identified.");
    }

    @Test
    void testPrintHeapAtGc() {
        String opts = "-Xss128k -XX:+PrintHeapAtGC -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_HEAP_AT_GC),
                Analysis.INFO_JDK8_PRINT_HEAP_AT_GC + " analysis not identified.");
    }

    @Test
    void testPrintPromotionFailure() {
        String opts = "-Xss128k -Xmx4g -XX:+PrintPromotionFailure";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_PROMOTION_FAILURE),
                Analysis.INFO_JDK8_PRINT_PROMOTION_FAILURE + " analysis not identified.");
    }

    /**
     * Test if -XX:+PrintReferenceGC enabled
     */
    @Test
    void testPrintReferenceGC() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintReferenceGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_REFERENCE_GC_ENABLED),
                Analysis.INFO_JDK8_PRINT_REFERENCE_GC_ENABLED + " analysis not identified.");
    }

    /**
     * Test if -XX:+PrintStringDeduplicationStatistics enabled
     */
    @Test
    void testPrintStringDeduplicationStatistics() {
        String opts = "-Xss128k -XX:+PrintStringDeduplicationStatistics -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_STRING_DEDUP_STATS_ENABLED),
                Analysis.INFO_JDK8_PRINT_STRING_DEDUP_STATS_ENABLED + " analysis not identified.");
    }

    @Test
    void testPrintTenuringDistribution() {
        String opts = "-Xss128k -XX:+PrintTenuringDistribution -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION),
                Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION + " analysis not identified.");
    }

    /**
     * Verify analysis file property key/value lookup.
     */
    @Test
    void testPropertyKeyValueLookup() {
        Analysis[] analysis = Analysis.values();
        for (int i = 0; i < analysis.length; i++) {
            assertNotNull(analysis[i].getKey() + " not found.", analysis[i].getValue());
        }
    }

    @Test
    void testSafepointLogging() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:+PrintSafepointStatistics "
                + "-XX:PrintSafepointStatisticsCount=1 -XX:+LogVMOutput -XX:LogFile=/path/to/vm.log -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DIAGNOSTIC_PRINT_SAFEPOINT_STATISTICS),
                Analysis.WARN_DIAGNOSTIC_PRINT_SAFEPOINT_STATISTICS + " analysis not identified.");
    }

    @Test
    void testServerFlag() {
        String opts = "-Xss512 -server -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        // 64-bit is assumed
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_64_SERVER_REDUNDANT),
                Analysis.INFO_64_SERVER_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testShenandoahGuaranteedGCInterval() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:ShenandoahGuaranteedGCInterval=20000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_SHENANDOAH_GUARANTEED_GC_INTERVAL),
                Analysis.WARN_SHENANDOAH_GUARANTEED_GC_INTERVAL + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testShenandoahUncommitDelay() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:ShenandoahUncommitDelay=5000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_SHENANDOAH_GUARANTEED_UNCOMMIT_DELAY),
                Analysis.WARN_SHENANDOAH_GUARANTEED_UNCOMMIT_DELAY + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testSignalHandlingDisabled() {
        String opts = "-Xss512 -Xmx33g -Xrs";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RS), Analysis.WARN_RS + " analysis not identified.");
    }

    @Test
    void testSurvivorRatio() {
        String opts = "-Xss128k -XX:SurvivorRatio=6 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_SURVIVOR_RATIO),
                Analysis.INFO_SURVIVOR_RATIO + " analysis not identified.");
    }

    @Test
    void testTargetSurvivorRatio() {
        String opts = "-Xss128k -XX:TargetSurvivorRatio=90 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_SURVIVOR_RATIO_TARGET),
                Analysis.INFO_SURVIVOR_RATIO_TARGET + " analysis not identified.");
    }

    @Test
    void testTenuringDisabled() {
        String opts = "-Xss128k -XX:MaxTenuringThreshold=0 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_TENURING_DISABLED),
                Analysis.WARN_TENURING_DISABLED + " analysis not identified.");
    }

    @Test
    void testTenuringMissing() {
        String opts = "-Xss128k -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_MAX_TENURING_OVERRIDE),
                Analysis.INFO_MAX_TENURING_OVERRIDE + " analysis incorrectly identified.");
    }

    @Test
    void testTenuringNotDefault() {
        String opts = "-Xss128k -XX:MaxTenuringThreshold=8 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MAX_TENURING_OVERRIDE),
                Analysis.INFO_MAX_TENURING_OVERRIDE + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyAggressive() {
        String opts = "-Xss128k -XX:ThreadPriorityPolicy=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE),
                Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyAggressiveBackdoor() {
        String opts = "-Xss128k -XX:ThreadPriorityPolicy=42 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE_BACKDOOR),
                Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE_BACKDOOR + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyBad() {
        String opts = "-Xss128k -XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=-1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT),
                Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_BAD),
                Analysis.WARN_THREAD_PRIORITY_POLICY_BAD + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyIgnored() {
        String opts = "-Xss128k -XX:-UseThreadPriorities -XX:ThreadPriorityPolicy=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_IGNORED),
                Analysis.WARN_THREAD_PRIORITY_POLICY_IGNORED + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyMissing() {
        String opts = "-Xss128k -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT),
                Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_BAD),
                Analysis.WARN_THREAD_PRIORITY_POLICY_BAD + " analysis incorrectly identified.");
    }

    @Test
    void testThreadPriorityPolicyRedundant() {
        String opts = "-Xss128k -XX:ThreadPriorityPolicy=0 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_THREAD_PRIORITY_POLICY_REDUNDANT),
                Analysis.INFO_THREAD_PRIORITY_POLICY_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testThreadStackSizeTiny() {
        String opts = "-Xss512 -XX:TargetSurvivorRatio=90 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_STACK_SIZE_TINY),
                Analysis.WARN_THREAD_STACK_SIZE_TINY + " analysis not identified.");
    }

    @Test
    void testTieredCompilation() {
        String opts = "-Xss128k -XX:+TieredCompilation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TIERED_COMPILATION_ENABLED),
                Analysis.INFO_TIERED_COMPILATION_ENABLED + " analysis not identified.");
    }

    @Test
    void testTraceClassLoading() {
        String opts = "-Xss128k -XX:+TraceClassLoading -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TRACE_CLASS_LOADING),
                Analysis.INFO_TRACE_CLASS_LOADING + " analysis not identified.");
    }

    @Test
    void testTraceClassUnloading() {
        String opts = "-Xss128k -XX:+TraceClassUnloading -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TRACE_CLASS_UNLOADING),
                Analysis.INFO_TRACE_CLASS_UNLOADING + " analysis not identified.");
    }

    @Test
    void testUnlockDiagnosticVMOptions() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testUnlockExperimentalVMOptions() {
        String opts = "-Xss128k -XX:+UnlockExperimentalVMOptions -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testUseAdaptiveSizePolicyDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-UseAdaptiveSizePolicy";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_ADAPTIVE_SIZE_POLICY_DISABLED),
                Analysis.WARN_ADAPTIVE_SIZE_POLICY_DISABLED + " analysis not identified.");
    }

    @Test
    void testUseCondCardMark() {
        String opts = "-Xss128k -XX:+UseCondCardMark -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_USE_COND_CARD_MARK),
                Analysis.WARN_USE_COND_CARD_MARK + " analysis not identified.");
    }

    @Test
    void testUseFastUnorderedTimeStamps() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:+UseFastUnorderedTimeStamps";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_FAST_UNORDERED_TIMESTAMPS),
                Analysis.WARN_FAST_UNORDERED_TIMESTAMPS + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testUseMembar() {
        String opts = "-Xss128k -Xms2048M -Xmx4096M -XX:+UseMembar";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_USE_MEMBAR),
                Analysis.WARN_USE_MEMBAR + " analysis not identified.");
    }

    @Test
    void testUseParallelOldGcDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelGC -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_PARALLEL_OLD_DISABLED),
                Analysis.WARN_JDK11_PARALLEL_OLD_DISABLED + " analysis not identified.");
    }

    @Test
    void testUseParallelOldGcRedundant() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelGC -XX:+UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK11_PARALLEL_OLD_REDUNDANT),
                Analysis.INFO_JDK11_PARALLEL_OLD_REDUNDANT + " analysis not identified.");
    }

    /**
     * Test if PAR_NEW collector disabled with -XX:-UseParNewGC with the CMS collector.
     */
    @Test
    void testUseParNewGcDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:-UseParNewGC "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED),
                Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testUseParNewGcRedundant() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:+UseParNewGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_REDUNDANT),
                Analysis.INFO_JDK8_CMS_PAR_NEW_REDUNDANT + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED),
                Analysis.WARN_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testUseThreadPrioritiesDisabled() {
        String opts = "-Xss128k -XX:-UseThreadPriorities -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_USE_THREAD_PRIORITIES_DISABLED),
                Analysis.WARN_USE_THREAD_PRIORITIES_DISABLED + " analysis not identified.");
    }

    @Test
    void testUseThreadPrioritiesRedundant() {
        String opts = "-Xss128k -XX:+UseThreadPriorities -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT),
                Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testUseVmInterruptibleIo() {
        String opts = "-Xss512 -Xmx33g -XX:-UseVMInterruptibleIO";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_USE_VM_INTERRUPTIBLE_IO),
                Analysis.WARN_JDK8_USE_VM_INTERRUPTIBLE_IO + " analysis not identified.");
    }

    @Test
    void testVerboseClass() {
        String opts = "-Xss128k -verbose:class -Xmx2G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_VERBOSE_CLASS),
                Analysis.INFO_VERBOSE_CLASS + " analysis not identified.");
    }

}
