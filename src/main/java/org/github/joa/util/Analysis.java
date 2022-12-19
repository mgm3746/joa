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

/**
 * Analysis constants.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public enum Analysis {

    /**
     * Property key for limiting a multi-thread garbage collector to a single thread with -XX:ParallelGCThreads=1.
     */
    ERROR_PARALLEL_GC_THREADS_1("error.parallel.gc.threads.1"),

    /**
     * Property key for remote debugging enabled.
     */
    ERROR_REMOTE_DEBUGGING_ENABLED("error.remote.debugging.enabled"),

    /**
     * Property key for setting the number of compiler threads (-XX:CICompilerCount=N).
     */
    INFO_CI_COMPILER_COUNT("info.ci.compiler.count"),

    /**
     * Property key for the CMS collector disabled in JDK11.
     */
    INFO_CMS_DISABLED("info.cms.disabled"),

    /**
     * Property key for -XX:CMSInitiatingOccupancyFraction without -XX:+UseCMSInitiatingOccupancyOnly.
     */
    INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING("info.cms.init.occupancy.only.missing"),

    /**
     * Property key for compressed class pointers size set (-XX:CompressedClassSpaceSize) with compressed class pointers
     * disabled (-XX:+UseCompressedClassPointers).
     */
    INFO_COMP_CLASS_SIZE_COMP_CLASS_DISABLED("info.comp.class.size.comp.class.disabled"),
    /**
     * Property key for compressed class pointers size set (-XX:CompressedClassSpaceSize) with compressed object
     * references disabled (-XX:-UseCompressedOops).
     */
    INFO_COMP_CLASS_SIZE_COMP_OOPS_DISABLED("info.comp.class.size.comp.oops.disabled"),

    /**
     * Property key for -XX:CompileThreshold when tiered compilation is enabled.
     */
    INFO_COMPILE_THRESHOLD_IGNORED("info.compile.threshold.ignored"),

    /**
     * Property key for -XX:-ExplicitGCInvokesConcurrentAndUnloadsClasses in combination with -XX:+DisableExplicitGC.
     */
    INFO_CRUFT_EXP_GC_INV_CON_AND_UNL_CLA("info.cruft.exp.gc.inv.con.and.unl.cla"),

    /**
     * Property key for enable debugging using the Java Virtual Machine Debug Interface (JVMDI) with -Xdebug.
     */
    INFO_DEBUG("info.debug"),

    /**
     * Property key for -XX:+UnlockDiagnosticVMOptions.
     */
    INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED("info.diagnostic.vm.options.enabled"),

    /**
     * Property key for -XX:+EliminateLocks.
     */
    INFO_ELIMINATE_LOCKS_ENABLED("info.eliminate.locks.enabled"),

    /**
     * Property key for GC log being sent to stdout.
     */
    INFO_GC_LOG_STDOUT("info.gc.log.stdout"),

    /**
     * Property key for heap dump on out of memory error option missing.
     */
    INFO_HEAP_DUMP_ON_OOME_MISSING("info.heap.dump.on.oome.missing"),

    /**
     * Property key for heap dumps enabled without specifying a location with the -XX:HeapDumpPath option.
     */
    INFO_HEAP_DUMP_PATH_MISSING("info.heap.dump.path.missing"),

    /**
     * Property key for the maximum heap size not being explicitly set.
     */
    INFO_HEAP_MAX_MISSING("info.heap.max.missing"),

    /**
     * Property key for instrumentation.
     */
    INFO_INSTRUMENTATION("info.instrumentation"),

    /**
     * Property key for the PARALLEL_OLD collector enabled/disabled without the parallel collector being used.
     */
    INFO_JDK11_PARALLEL_OLD_CRUFT("info.jdk11.parallel.old.cruft"),

    /**
     * Property key for redundant option -XX:+UseParallelOldGC.
     */
    INFO_JDK11_PARALLEL_OLD_REDUNDANT("info.jdk11.parallel.old.redundant"),

    /**
     * Property key for the PAR_NEW collector enabled/disabled without the CMS collector being used.
     */
    INFO_JDK8_CMS_PAR_NEW_CRUFT("info.jdk8.cms.par.new.cruft"),

    /**
     * Property key for redundant option -XX:-UseParNewGC.
     */
    INFO_JDK8_CMS_PAR_NEW_REDUNDANT("info.jdk8.cms.par.new.redundant"),

    /**
     * Property key for disabling Adaptive Resize Policy output with -XX:-PrintAdaptiveSizePolicy.
     */
    INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_DISABLED("info.jdk8.print.adaptive.resize.plcy.disabled"),

    /**
     * Property key for enabling Adaptive Resize Policy output with -XX:+PrintAdaptiveSizePolicy.
     */
    INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_ENABLED("info.jdk8.print.adaptive.resize.plcy.enabled"),

    /**
     * Property key for CMS Free List Space statistics being output.
     */
    INFO_JDK8_PRINT_FLS_STATISTICS("info.jdk8.print.fls.statistics"),

    /**
     * Property key for -XX:+PrintGCCause.
     */
    INFO_JDK8_PRINT_GC_CAUSE("info.jdk8.print.gc.cause"),

    /**
     * Property key for printing additional heap data (-XX:+PrintHeapAtGC).
     */
    INFO_JDK8_PRINT_HEAP_AT_GC("info.jdk8.print.heap.at.gc"),

    /**
     * Property key for -XX:+PrintPromotionFailure.
     */
    INFO_JDK8_PRINT_PROMOTION_FAILURE("info.jdk8.print.promotion.failure"),

    /**
     * Property key for -XX:+PrintReferenceGC.
     */
    INFO_JDK8_PRINT_REFERENCE_GC_ENABLED("info.jdk8.print.reference.gc.enabled"),

    /**
     * Property key for -XX:+PrintStringDeduplicationStatistics.
     */
    INFO_JDK8_PRINT_STRING_DEDUP_STATS_ENABLED("info.jdk8.print.string.dedup.stats.enabled"),

    /**
     * Property key for outputting tenuring distribution information (-XX:+PrintTenuringDistribution).
     */
    INFO_JDK8_PRINT_TENURING_DISTRIBUTION("info.jdk8.print.tenuring.distribution"),

    /**
     * Property key for using the deprecated -Xloggc option on JDK9+.
     */
    INFO_JDK9_DEPRECATED_LOGGC("info.jdk9.deprecated.loggc"),

    /**
     * Property key for using the deprecated -XX:+PrintGC option on JDK9+.
     */
    INFO_JDK9_DEPRECATED_PRINT_GC("info.jdk9.deprecated.print.gc"),

    /**
     * Property key for using the deprecated -XX:+PrintGCDetails option on JDK9+.
     */
    INFO_JDK9_DEPRECATED_PRINT_GC_DETAILS("info.jdk9.deprecated.print.gc.details"),

    /**
     * Property key for JDK Flight Recorder (JFR) being used.
     */
    INFO_JFR("info.jfr"),

    /**
     * Property key for JMX enabled with -Dcom.sun.management.jmxremote or -XX:+ManagementServer.
     */
    INFO_JMX_ENABLED("info.jmx.enabled"),

    /**
     * Property key for maximum permanent generation size being set.
     */
    INFO_MAX_PERM_SIZE("info.max.perm.size"),

    /**
     * Property key for overriding the number of times an object is copied between survivor spaces being set with
     * -XX:MaxTenuringThreshold=N (0-15). 0 = disabled. 15 (default) = promote when the survivor space fills. Unless
     * testing has shown this improves performance, consider removing this option to allow the default value to be
     * applied.
     */
    INFO_MAX_TENURING_OVERRIDE("info.max.tenuring.override"),

    /**
     * Property key for metaspace initial and/or max size being set.
     */
    INFO_METASPACE("info.metaspace"),

    /**
     * Property key for metaspace including only class metadata.
     */
    INFO_METASPACE_CLASS_METADATA("info.metaspace.class.metadata"),

    /**
     * Property key for metaspace including class metadata and compressed class space.
     */
    INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE("info.metaspace.class.metadata.and.comp.class.space"),

    /**
     * Property key for setting the minimum amount to resize the heap space in bytes with -XX:MinHeapDeltaBytes=N.
     * 
     */
    INFO_MIN_HEAP_DELTA_BYTES("info.min.heap.delta.bytes"),

    /**
     * Property key for native agent library.
     */
    INFO_NATIVE_AGENT("info.native.agent"),

    /**
     * Property key for young space &gt;= old space.
     */
    INFO_NEW_RATIO_INVERTED("info.new.ratio.inverted"),

    /**
     * Property key for explicitly setting the number of parallel garbage collector threads (-XX:ParallelGCThreads=N).
     */
    INFO_PARALLEL_GC_THREADS("info.parallel.gc.threads"),

    /**
     * Property key for setting the number of parallel garbage collector threads (-XX:ParallelGCThreads=N) with the
     * serial collector.
     */
    INFO_PARALLEL_GC_THREADS_SERIAL("info.parallel.gc.threads.serial"),

    /**
     * Property key for initial permanent generation size being set.
     */
    INFO_PERM_SIZE("info.perm.size"),

    /**
     * Property key for printing application concurrent time (-XX:+PrintGCApplicationConcurrentTime).
     */
    INFO_PRINT_GC_APPLICATION_CONCURRENT_TIME("info.print.gc.application.concurrent.time"),

    /**
     * Property key for -Dsun.rmi.dgc.client.gcInterval.redundant in combination with -XX:+DisableExplicitGC.
     */
    INFO_RMI_DGC_CLIENT_GCINTERVAL_REDUNDANT("info.rmi.dgc.client.gcInterval.redundant"),

    /**
     * Property key for -Dsun.rmi.dgc.server.gcInterval.redundant in combination with -XX:+DisableExplicitGC.
     */
    INFO_RMI_DGC_SERVER_GCINTERVAL_REDUNDANT("info.rmi.dgc.server.gcInterval.redundant"),

    /**
     * Property key for the survivor ratio being set with -XX:SurvivorRatio=N (e.g. -XX:SurvivorRatio=6 ).
     * 
     */
    INFO_SURVIVOR_RATIO("info.survivor.ratio"),

    /**
     * Property key for the target survivor ratio being set with XX:TargetSurvivorRatio=N (e.g.
     * -XX:TargetSurvivorRatio=90).
     * 
     */
    INFO_SURVIVOR_RATIO_TARGET("info.survivor.ratio.target"),

    /**
     * Property key for -XX:+TieredCompilation.
     */
    INFO_TIERED_COMPILATION_ENABLED("info.tiered.compilation.enabled"),

    /**
     * Property key for outputting class loading information (-XX:+TraceClassLoading).
     */
    INFO_TRACE_CLASS_LOADING("info.trace.class.loading"),

    /**
     * Property key for outputting class unloading information (-XX:+TraceClassUnloading).
     */
    INFO_TRACE_CLASS_UNLOADING("info.trace.class.unloading"),

    /**
     * Property key for undefined JVM option(s).
     */
    INFO_UNDEFINED("info.undefined"),

    /**
     * Property key for class loading logging (sent to standard out) enabled with -verbose:class.
     */
    INFO_VERBOSE_CLASS("info.verbose.class"),

    /**
     * Property key for adaptive size policy disabled with -XX:-UseAdaptiveSizePolicy.
     */
    WARN_ADAPTIVE_SIZE_POLICY_DISABLED("warn.adaptive.size.policy.disabled"),

    /**
     * Property key for biased locking disabled (-XX:-UseBiasedLocking).
     */
    WARN_BIASED_LOCKING_DISABLED("warn.biased.locking.disabled"),

    /**
     * Property key for disabling compiling bytecode in the background.
     */
    WARN_BYTECODE_BACK_COMP_DISABLED("warn.bytecode.back.comp.disabled"),

    /**
     * Property key for bytecode compilation disabled.
     */
    WARN_BYTECODE_COMPILE_DISABLED("warn.bytecode.compile.disabled"),

    /**
     * Property key for precompiling bytecode.
     */
    WARN_BYTECODE_COMPILE_FIRST_INVOCATION("warn.bytecode.compile.first.invocation"),

    /**
     * Property key for -XX:+UseCGroupMemoryLimitForHeap.
     */
    WARN_CGROUP_MEMORY_LIMIT("warn.cgroup.memory.limit"),

    /**
     * Property key for -XX:+UseCGroupMemoryLimitForHeap in combination with -Xmx/-XX:MaxHeapSize.
     */
    WARN_CGROUP_MEMORY_LIMIT_OVERRIDE("warn.cgroup.memory.limit.override"),

    /**
     * Property key for class unloading disabled with -XX:-ClassUnloading.
     */
    WARN_CLASS_UNLOADING_DISABLED("warn.class.unloading.disabled"),

    /**
     * Property key for CMS collector class unloading disabled.
     */
    WARN_CMS_CLASS_UNLOADING_DISABLED("warn.cms.class.unloading.disabled"),

    /**
     * Property key for specifying both the CMS collector running in incremental mode and an initiating occupancy
     * fraction.
     */
    WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT("warn.cms.inc.mode.with.init.occup.fract"),

    /**
     * Property key for multi-threaded CMS initial mark disabled with -XX:-CMSParallelInitialMarkEnabled.
     */
    WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED("warn.cms.parallel.initial.mark.disabled"),

    /**
     * Property key for multi-threaded CMS remark disabled with -XX:-CMSParallelRemarkEnabled.
     */
    WARN_CMS_PARALLEL_REMARK_DISABLED("warn.cms.parallel.remark.disabled"),

    /**
     * Property key for compressed class pointers disabled (-XX:-UseCompressedClassPointers), and heap &lt; 32G.
     */
    WARN_COMP_CLASS_DISABLED_HEAP_LT_32G("warn.comp.class.disabled.heap.lt.32g"),

    /**
     * Property key for compressed class pointers disabled (-XX:-UseCompressedClassPointers), and heap size unknown.
     */
    WARN_COMP_CLASS_DISABLED_HEAP_UNK("warn.comp.class.disabled.heap.unk"),

    /**
     * Property key for compressed class pointers enabled (-XX:+UseCompressedClassPointers), and heap &gt;= 32G.
     */
    WARN_COMP_CLASS_ENABLED_HEAP_GT_32G("warn.comp.class.enabled.heap.gt.32g"),

    /**
     * Property key for compressed class pointers space size set (-XX:CompressedClassSpaceSize), and heap &gt;= 32G.
     */
    WARN_COMP_CLASS_SIZE_HEAP_GT_32G("warn.comp.class.size.heap.gt.32g"),

    /**
     * Property key for compressed object references disabled (-XX:-UseCompressedOops), and heap &lt; 32G.
     */
    WARN_COMP_OOPS_DISABLED_HEAP_LT_32G("warn.comp.oops.disabled.heap.lt.32g"),

    /**
     * Property key for compressed object references disabled (-XX:-UseCompressedOops), and heap size unknown.
     */
    WARN_COMP_OOPS_DISABLED_HEAP_UNK("warn.comp.oops.disabled.heap.unk"),

    /**
     * Property key for compressed object references enabled (-XX:+UseCompressedOops), and heap &gt;= 32G.
     */
    WARN_COMP_OOPS_ENABLED_HEAP_GT_32G("warn.comp.oops.enabled.heap.gt.32g"),

    /**
     * Property key for -Xconcurrentio.
     */
    WARN_CONCURRENTIO("warn.concurrentio"),

    /**
     * Property key for safepoint statistics logging.
     */
    WARN_DIAGNOSTIC_DEBUG_NON_SAFEPOINTS("warn.diagnostic.debug.non.safepoints"),

    /**
     * Property key for safepoint statistics logging.
     */
    WARN_DIAGNOSTIC_PRINT_SAFEPOINT_STATISTICS("warn.diagnostic.print.safepoint.statistics"),

    /**
     * Property key for parallel class loading enabled.
     */
    WARN_DIAGNOSTIC_UNSYNCLOAD_CLASS("warn.diagnostic.unsyncload.class"),

    /**
     * Property key for guaranteed safepoint interval being set.
     */
    WARN_DIAGNOSTICS_GUARANTEED_SAFEPOINT_INTERVAL("warn.diagnostic.guaranteed.safepoint.interval"),

    /**
     * Property key for the creation of the AttachListener socket file (/tmp/.java_pid&lt;pid&gt;) used by
     * jcmd/jmap/jstack to communicate with the JVM being disabled.
     */
    WARN_DISABLE_ATTACH_MECHANISM("warn.disable.attach.mechanism"),

    /**
     * Property key for experimental jvm options enabled with <code>-XX:+UnlockExperimentalVMOptions</code>.
     */
    WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED("warn.experimental.vm.options.enabled"),

    /**
     * Property key for explicit garbage collection disabled.
     */
    WARN_EXPLICIT_GC_DISABLED("warn.explicit.gc.disabled"),

    /**
     * Property key for explicit garbage collection disabled and specifying concurrent collections.
     */
    WARN_EXPLICIT_GC_DISABLED_CONCURRENT("warn.explicit.gc.disabled.concurrent"),

    /**
     * Property key for fast unordered timestamps (experimental) enabled with
     * <code>-XX:+UseFastUnorderedTimeStamps</code>.
     */
    WARN_FAST_UNORDERED_TIMESTAMPS("warn.fast.unordered.timestamps"),

    /**
     * Property key for the occupancy threshold for a region to be considered as a candidate region for a G1_CLEANUP
     * collection being specified with <code>-XX:G1MixedGCLiveThresholdPercent=NN</code>.
     */
    WARN_G1_MIXED_GC_LIVE_THRSHOLD_PRCNT("warn.g1.mixed.gc.live.thrshld.prcnt"),

    /**
     * Property key for heap dump on memory error option disabled.
     */
    WARN_HEAP_DUMP_ON_OOME_DISABLED("warn.heap.dump.on.oome.disabled"),

    /**
     * Property key for heap dump filename specified.
     */
    WARN_HEAP_DUMP_PATH_FILENAME("warn.heap.dump.path.filename"),

    /**
     * Property key for JDK11 gc log file with static name that will be overwritten on JVM startup.
     */
    WARN_JDK11_GC_LOG_FILE_OVERWRITE("warn.jdk11.gc.log.file.overwrite"),

    /**
     * Property key for specifying the number of GC log files (filecount=N) to keep with log rotation is disabled
     * (filecount=0).
     */
    WARN_JDK11_GC_LOG_FILE_ROTATION_DISABLED("warn.jdk11.gc.log.file.rotation.disabled"),

    /**
     * Property key for automatic GC log file rotation disabled with filesize=0.
     */
    WARN_JDK11_GC_LOG_FILE_SIZE_0("warn.jdk11.gc.log.file.size.0"),

    /**
     * Property key for specifying the gc log file size that triggers rotation (filesize=N[K|M|G]) is small (&lt; 5M).
     */
    WARN_JDK11_GC_LOG_FILE_SIZE_SMALL("warn.jdk11.gc.log.file.size.small"),

    /**
     * Property key for the multi-threaded parallel old collector (default) disabled with -XX:-UseParallelOldGC.
     */
    WARN_JDK11_PARALLEL_OLD_DISABLED("warn.jdk11.parallel.old.disabled"),

    /**
     * Property key for PAR_NEW collector disabled, resulting in the SERIAL collector being used in combination with the
     * CMS collector.
     */
    WARN_JDK8_CMS_PAR_NEW_DISABLED("warn.jdk8.cms.par.new.disabled"),

    /**
     * Property key for gc log file with a static name that will be overwritten on JVM startup.
     */
    WARN_JDK8_GC_LOG_FILE_OVERWRITE("warn.jdk8.gc.log.file.overwrite"),

    /**
     * Property key for GC log file rotation disabled (-XX:-UseGCLogFileRotation).
     */
    WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED("warn.jdk8.gc.log.file.rotation.disabled"),

    /**
     * Property key for specifying the number of GC log files (-XX:NumberOfGCLogFiles) to keep with log rotation is
     * disabled (-XX:-UseGCLogFileRotation).
     */
    WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED_NUM("warn.jdk8.gc.log.file.rotation.disabled.num"),

    /**
     * Property key for GC log file rotation not enabled in JDK8 (-XX:+UseGCLogFileRotation -XX:GCLogFileSize=N
     * -XX:NumberOfGCLogFiles=N).
     */
    WARN_JDK8_GC_LOG_FILE_ROTATION_NOT_ENABLED("warn.jdk8.gc.log.file.rotation.not.enabled"),

    /**
     * Property key for specifying the gc log file size that triggers rotation (-XX:GCLogFileSize=N[K|M|G]) is small
     * (&lt; 5M).
     */
    WARN_JDK8_GC_LOG_FILE_SIZE_SMALL("warn.jdk8.gc.log.file.size.small"),

    /**
     * Property key for -XX:-PrintGCCause.
     */
    WARN_JDK8_PRINT_GC_CAUSE_DISABLED("warn.jdk8.print.gc.cause.disabled"),

    /**
     * Property key for gc details option disabled.
     */
    WARN_JDK8_PRINT_GC_DETAILS_DISABLED("warn.jdk8.print.gc.details.disabled"),

    /**
     * Property key for -XX:-UseVMInterruptibleIO.
     */
    WARN_JDK8_USE_VM_INTERRUPTIBLE_IO("warn.jdk8.use.vm.interruptible.io"),

    /**
     * Property key for MaxMetaspaceSize less than CompressedClassSpaceSize. MaxMetaspaceSize includes
     * CompressedClassSpaceSize, so MaxMetaspaceSize should be larger than CompressedClassSpaceSize.
     */
    WARN_METASPACE_LT_COMP_CLASS("warn.metaspace.lt.comp.class"),

    /**
     * Property key for printing a class histogram when a thread dump is initiated (-XX:+PrintClassHistogram).
     */
    WARN_PRINT_CLASS_HISTOGRAM("warn.print.class.histogram"),

    /**
     * Property key for printing a class histogram when a thread dump is initiated
     * (-XX:+PrintClassHistogramAfterFullGC).
     */
    WARN_PRINT_CLASS_HISTOGRAM_AFTER_FULL_GC("warn.print.class.histogram.after.full.gc"),

    /**
     * Property key for printing a class histogram when a thread dump is initiated
     * (-XX:+PrintClassHistogramBeforeFullGC).
     */
    WARN_PRINT_CLASS_HISTOGRAM_BEFORE_FULL_GC("warn.print.class.histogram.before.full.gc"),

    /**
     * Property key for large (&gt;24 hours) sun.rmi.dgc.client.gcInterval.
     */
    WARN_RMI_DGC_CLIENT_GCINTERVAL_LARGE("warn.rmi.dgc.client.gcInterval.large"),

    /**
     * Property key for small (&lt;1 hour) sun.rmi.dgc.client.gcInterval.
     */
    WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL("warn.rmi.dgc.client.gcInterval.small"),

    /**
     * Property key for large (&gt;24 hours) sun.rmi.dgc.server.gcInterval.
     */
    WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE("warn.rmi.dgc.server.gcInterval.large"),

    /**
     * Property key for small (&lt;1 hour)sun.rmi.dgc.server.gcInterval.
     */
    WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL("warn.rmi.dgc.server.gcInterval.small"),

    /**
     * Property key for -Xrs disabling JVM signal handling.
     */
    WARN_RS("warn.rs"),

    /**
     * Property key for setting the number of milliseconds for a guaranteed GC cycle with
     * -XX:ShenandoahGuaranteedGCInterval=N.
     */
    WARN_SHENANDOAH_GUARANTEED_GC_INTERVAL("warn.shenandoah.guaranteed.gc.interval"),

    /**
     * Property key for setting the number of milliseconds before unused memory in the page cache is evicted with
     * -XX:ShenandoahUncommitDelay=N.
     */
    WARN_SHENANDOAH_GUARANTEED_UNCOMMIT_DELAY("warn.shenandoah.uncommit.delay"),

    /**
     * Property key for disabling tenuring with -XX:MaxTenuringThreshold=0 or by setting it to a value greater than 15
     * (e.g. -XX:MaxTenuringThreshold=32).
     */
    WARN_TENURING_DISABLED("warn.tenuring.disabled"),

    /**
     * Property key for -XX:+UseMembar.
     */
    WARN_USE_MEMBAR("warn.use.membar"),

    /**
     * Property key for class verification on loading disabled with -Xverify:none.
     */
    WARN_VERIFY_NONE("warn.verify.none");

    private String key;

    private Analysis(final String key) {
        this.key = key;
    }

    /**
     * @return Analysis property file key.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return Analysis property file value.
     */
    public String getValue() {
        return JoaUtil.getPropertyValue(Constants.ANALYSIS_PROPERTY_FILE, key);
    }

    @Override
    public String toString() {
        return this.getKey();
    }
}
