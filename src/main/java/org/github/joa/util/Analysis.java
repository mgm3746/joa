/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022-2023 Mike Millson                                                                                    *
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
     * Property key for not specifying the CMS collector be used for old collections, causing the CMS_SERIAL_OLD
     * collector to be used by default.
     */
    ERROR_CMS_MISSING("error.cms.missing"),

    /**
     * Property key for duplicate JVM options.
     */
    ERROR_DUPS("error.dups"),

    /**
     * Property key for the JVM ignoring the JVM option to use the G1 collector and using the parallel collector
     * instead.
     */
    ERROR_G1_IGNORED_PARALLEL("error.g1.ignored.parallel"),

    /**
     * Property key for the JVM ignoring the JVM option(s) GC collector(s) and using a different collector instead.
     */
    ERROR_GC_IGNORED("error.gc.ignored"),

    /**
     * Property key for PAR_NEW collector disabled, resulting in the SERIAL collector being used in combination with the
     * CMS collector.
     */
    ERROR_JDK8_CMS_PAR_NEW_DISABLED("error.jdk8.cms.par.new.disabled"),

    /**
     * Property key for -XX:+UseCMSCompactAtFullCollection causing full collections being handled by the concurrent CMS
     * collector, a normal "background" collection being run in the "foreground" within a safepoint instead of the
     * default SERIAL_OLD collector.
     */
    ERROR_JDK8_USE_CMS_COMPACTION_AT_FULL_GC_ENABLED("error.jdk8.use.cms.compaction.at.full.collection.enabled"),

    /**
     * Property key for using the PAR_NEW collector in combination with CMS_SERIAL_OLD.
     */
    ERROR_PAR_NEW_SERIAL_OLD("error.par.new.serial.old"),

    /**
     * Property key for limiting a multi-thread garbage collector to a single thread with -XX:ParallelGCThreads=1.
     */
    ERROR_PARALLEL_GC_THREADS_1("error.parallel.gc.threads.1"),

    /**
     * Property key for using the PARALLEL_SCAVENGE in combination with PARALLEL_SERIAL_OLD.
     */
    ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD("error.parallel.scavenge.parallel.serial.old"),

    /**
     * Property key for InitialRAMPercentage &gt;= 100.
     */
    ERROR_RAM_PCT_INITIAL_100("error.ram.pct.initial.100"),

    /**
     * Property key for InitialRAMPercentage &gt; MaxRAMPercentage.
     */
    ERROR_RAM_PCT_INITIAL_GT_MAX("error.ram.pct.initial.gt.max"),

    /**
     * Property key for InitialRAMPercentage &gt; MinRAMPercentage.
     */
    ERROR_RAM_PCT_INITIAL_GT_MIN("error.ram.pct.initial.gt.min"),

    /**
     * Property key for MaxRAMPercentage &gt;= 100.
     */
    ERROR_RAM_PCT_MAX_100("error.ram.pct.max.100"),

    /**
     * Property key for MinRAMPercentage &gt;= 100.
     */
    ERROR_RAM_PCT_MIN_100("error.ram.pct.min.100"),

    /**
     * Property key for remote debugging enabled.
     */
    ERROR_REMOTE_DEBUGGING_ENABLED("error.remote.debugging.enabled"),

    /**
     * Property key for the -client flag on 64-bit.
     */
    INFO_64_CLIENT("info.64.client"),

    /**
     * Property key for -d64 flag on 64-bit.
     */
    INFO_64_D64_REDUNDANT("info.64.d64.redundant"),

    /**
     * Property key for -server flag on 64-bit.
     */
    INFO_64_SERVER_REDUNDANT("info.64.server.redundant"),

    /**
     * Property key for explicitly setting cpu/cores with -XX:ActiveProcessorCount=N and overriding default determined
     * by container support reading cgroup settings.
     */
    INFO_ACTIVE_PROCESSOR_COUNT("info.active.processor.count"),

    /**
     * Property key for setting the number of compiler threads (-XX:CICompilerCount=N).
     */
    INFO_CI_COMPILER_COUNT("info.ci.compiler.count"),

    /**
     * Property key for the CMS collector disabled in JDK11.
     */
    INFO_CMS_DISABLED("info.cms.disabled"),

    /**
     * Property key for setting the flag to always record CMS parallel initial mark/remark eden chunks (e.g.
     * -XX:-CMSEdenChunksRecordAlways).
     */
    INFO_CMS_EDEN_CHUNK_RECORD_ALWAYS("info.cms.eden.chunks.record.always"),

    /**
     * Property key for CMS collector running in incremental mode.
     */
    INFO_CMS_INCREMENTAL_MODE("info.cms.incremental.mode"),

    /**
     * Property key for -XX:CMSInitiatingOccupancyFraction without -XX:+UseCMSInitiatingOccupancyOnly.
     */
    INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING("info.cms.init.occupancy.only.missing"),

    /**
     * Property key for setting the milliseconds the CMS collector will wait before starting an initial mark after a
     * young collection with -XX:CMSWaitDuration=N.
     */
    INFO_CMS_WAIT_DURATION("info.cms.wait.duration"),

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
     * Property key for fast unordered timestamps enabled by JVM ergonomics.
     */
    INFO_FAST_UNORDERED_TIMESTAMPS("info.fast.unordered.timestamps"),

    /**
     * Property key for summarized remembered set processing output.
     */
    INFO_G1_SUMMARIZE_RSET_STATS_OUTPUT("info.g1.summarize.rset.stats.output"),

    /**
     * Property key for GC log being sent to stdout.
     */
    INFO_GC_LOG_STDOUT("info.gc.log.stdout"),

    /**
     * Property key for -XX:-UseGCOverheadLimit.
     */
    INFO_GC_OVERHEAD_LIMIT_DISABLED("info.gc.overhead.limit.disabled"),

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
     * Property key for min heap not equal to max heap.
     */
    INFO_HEAP_MIN_NOT_EQUAL_MAX("info.heap.min.not.equal.max"),

    /**
     * Property key for -XX:+IgnoreUnrecognizedVMOptions.
     */
    INFO_IGNORE_UNRECOGNIZED_VM_OPTIONS("info.ignore.unrecognized.vm.options"),

    /**
     * Property key for instrumentation.
     */
    INFO_INSTRUMENTATION("info.instrumentation"),

    /**
     * Property key for missing gc* to output details at gc needed for analysis.
     */
    INFO_JDK11_PRINT_GC_DETAILS_MISSING("info.jdk11.print.gc.details.missing"),

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
     * Property key for -XX:+PrintStringTableStatistics.
     */
    INFO_JDK8_PRINT_STRING_TABLE_STATS_ENABLED("info.jdk8.print.string.table.stats.enabled"),

    /**
     * Property key for outputting tenuring distribution information (-XX:+PrintTenuringDistribution).
     */
    INFO_JDK8_PRINT_TENURING_DISTRIBUTION("info.jdk8.print.tenuring.distribution"),

    /**
     * Property key for outputting tenuring distribution information (-XX:-PrintTenuringDistribution).
     */
    INFO_JDK8_PRINT_TENURING_DISTRIBUTION_DISABLED("info.jdk8.print.tenuring.distribution.disabled"),

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
     * Property key for -XX:-FlightRecorder. The flag is deprecated in JDK13.
     */
    INFO_JFR_FLIGHT_RECORDER_DISABLED("info.jfr.flight.recorder.disabled"),

    /**
     * Property key for -XX:+FlightRecorder. Unnecessary since JDK8 u240 and deprecated in JDK13.
     */
    INFO_JFR_FLIGHT_RECORDER_ENABLED("info.jfr.flight.recorder.enabled"),

    /**
     * Property key for JMX enabled with -Dcom.sun.management.jmxremote or -XX:+ManagementServer.
     */
    INFO_JMX_ENABLED("info.jmx.enabled"),

    /**
     * Property key for -XX:LargePageSizeInBytes being extraneous on Linux.
     */
    INFO_LARGE_PAGE_SIZE_IN_BYTES_LINUX("info.large.page.size.in.bytes.linux"),

    /**
     * Property key for -XX:LargePageSizeInBytes being extraneous on Linux.
     */
    INFO_LARGE_PAGE_SIZE_IN_BYTES_WINDOWS("info.large.page.size.in.bytes.windows"),

    /**
     * Property key for -XX:(+|-)MaxFDLimit being used on OS other than solaris.
     */
    INFO_MAX_FD_LIMIT_IGNORED("info.max.fd.limit.ignored"),

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
     * Property key for -XX:OnOutOfMemoryError is being used to execute a command or script when the first
     * OnOutOfMemoryError happens.
     */
    INFO_ON_OOME("info.on.oome"),

    /**
     * Property key for -XX:OnOutOfMemoryError being used to shut down the JVM when the first OnOutOfMemoryError
     * happens.
     */
    INFO_ON_OOME_KILL("info.on.oome.kill"),

    /**
     * Property key for undefined JVM option(s).
     */
    INFO_OPTS_UNDEFINED("info.opts.undefined"),

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
     * Property key for the PARALLEL_OLD collector enabled/disabled without the parallel collector being used.
     */
    INFO_PARALLEL_OLD_CRUFT("info.parallel.old.cruft"),

    /**
     * Property key for redundant option -XX:+UseParallelOldGC.
     */
    INFO_PARALLEL_OLD_REDUNDANT("info.parallel.old.redundant"),

    /**
     * Property key for performance data disabled.
     */
    INFO_PERF_DATA_DISABLED("info.perf.data.disabled"),

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
     * Property key for setting the thread priority policy with -XX:ThreadPriorityPolicy=0.
     */
    INFO_THREAD_PRIORITY_POLICY_REDUNDANT("info.thread.priority.policy.redundant"),

    /**
     * Property key for -XX:-TieredCompilation.
     */
    INFO_TIERED_COMPILATION_DISABLED("info.tiered.compilation.disabled"),

    /**
     * Property key for -XX:+TieredCompilation.
     */
    INFO_TIERED_COMPILATION_ENABLED("info.tiered.compilation.enabled"),

    /**
     * Property key for outputting class loading information (-XX:+TraceClassLoading).
     */
    INFO_TRACE_CLASS_LOADING("info.trace.class.loading"),

    /**
     * Property key for outputting class loading information disabled (-XX:-TraceClassLoading).
     */
    INFO_TRACE_CLASS_LOADING_DISABLED("info.trace.class.loading.disabled"),

    /**
     * Property key for outputting class unloading information (-XX:+TraceClassUnloading).
     */
    INFO_TRACE_CLASS_UNLOADING("info.trace.class.unloading"),

    /**
     * Property key for outputting class unloading information disabled (-XX:-TraceClassUnloading).
     */
    INFO_TRACE_CLASS_UNLOADING_DISABLED("info.trace.class.unloading.disabled"),

    /**
     * Property key for otherwise unaccounted JVM options disabled.
     */
    INFO_UNACCOUNTED_OPTIONS_DISABLED("info.unaccounted.options.disabled"),

    /**
     * Property key for redundant option -XX:-UseStringDeduplication.
     */
    INFO_USE_STRING_DEDUPLICATION_REDUNDANT("info.use.string.deduplication.redundant"),

    /**
     * Property key for redundant option -XX:+UseStringDeduplication used with a collector other than G1 for 8u20 &lt;=
     * JDK &lt;= 17.
     */
    INFO_USE_STRING_DEDUPLICATION_UNSUPPORTED("info.use.string.deduplication.unsupported"),

    /**
     * Property key for -XX:+UseThreadPriorities.
     */
    INFO_USE_THREAD_PRIORITIES_REDUNDANT("info.use.thread.priorities.redundant"),

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
     * Property key for the JNI validation checks enabled with -Xcheck:jni.
     */
    WARN_CHECK_JNI_ENABLED("warn.check.jni.enabled"),

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
     * Property key for performance data written to disk (/tmp/hsperfdata*) in a cloud environment.
     */
    WARN_CONTAINER_PERF_DATA_DISK("warn.container.perf.data.disk"),

    /**
     * Property key for container support disabled with -XX:-UseContainerSupport.
     */
    WARN_CONTAINER_SUPPORT_DISABLED("warn.container.support.disabled"),

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
     * Property key for explicit garbage collection not collected concurrently.
     */
    WARN_EXPLICIT_GC_NOT_CONCURRENT("warn.explicit.gc.not.concurrent"),

    /**
     * Property key for fast unordered timestamps (experimental) enabled with
     * <code>-XX:+UnlockExperimentalVMOptions -XX:+UseFastUnorderedTimeStamps</code>.
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
     * Property key for the JDK8 prior to update 40.
     */
    WARN_JDK8_G1_PRIOR_U40("warn.jdk8.g1.prior.u40"),

    /**
     * Property key for the JDK8 prior to update 40 recommendations.
     */
    WARN_JDK8_G1_PRIOR_U40_RECS("warn.jdk8.g1.prior.u40.recs"),

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
     * Property key for adding option to output details at gc needed for analysis.
     */
    WARN_JDK8_PRINT_GC_DETAILS_MISSING("warn.jdk8.print.gc.details.missing"),

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
     * Property key for -XX:-OmitStackTraceInFastThrow being used to enable stack traces for the following exceptions:
     * NullPointerException, ArithmeticException, ArrayIndexOutOfBoundsException, ArrayStoreException,
     * ClassCastException.
     */
    WARN_OMIT_STACK_TRACE_IN_FAST_THROW_DISABLED("warn.omit.stack.trace.in.fast.throw.disabled"),

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
     * Property key for disabling tenuring with -XX:MaxTenuringThreshold=0 or by setting it to a value greater than 15
     * (e.g. -XX:MaxTenuringThreshold=32).
     */
    WARN_TENURING_DISABLED("warn.tenuring.disabled"),

    /**
     * Property key for setting the aggressive thread priority policy with -XX:ThreadPriorityPolicy=1.
     */
    WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE("warn.thread.priority.policy.aggressive"),

    /**
     * Property key for setting the aggressive thread priority policy with a value other than
     * -XX:ThreadPriorityPolicy=1. There was a bug in JDK8 where only -XX:ThreadPriorityPolicy=1 was checked for root
     * permissions on Linux, allowing a non-root user to set the aggressive policy with a value other than 1 (e.g.
     * -XX:ThreadPriorityPolicy=42). This back door has been closed in JDK11, which only allows values of 0 or 1.
     */
    WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE_BACKDOOR("warn.thread.priority.policy.aggressive.backdoor"),

    /**
     * Property key for the thread priority policy setting being ignored due to a bad value
     * (-XX:ThreadPriorityPolicy=-1).
     */
    WARN_THREAD_PRIORITY_POLICY_BAD("warn.thread.priority.policy.bad"),

    /**
     * Property key for the thread priority policy setting being ignored due to the Java Threads API disabled with
     * -XX:-UseThreadPriorities.
     */
    WARN_THREAD_PRIORITY_POLICY_IGNORED("warn.thread.priority.policy.ignored"),

    /**
     * Property key for thread stack size is large.
     */
    WARN_THREAD_STACK_SIZE_LARGE("warn.thread.stack.size.large"),

    /**
     * Property key for thread stack size not set on 32-bit.
     */
    WARN_THREAD_STACK_SIZE_NOT_SET_32("warn.thread.stack.size.not.set.32"),

    /**
     * Property key for a small thread stack size (&lt; 128k).
     */
    WARN_THREAD_STACK_SIZE_SMALL("warn.thread.stack.size.small"),

    /**
     * Property key for a tiny thread stack size (&lt; 1k).
     */
    WARN_THREAD_STACK_SIZE_TINY("warn.thread.stack.size.tiny"),

    /**
     * Property key for conditional dirty card marking enabled with -XX:+UseCondCardMark.
     */
    WARN_USE_COND_CARD_MARK("warn.use.cond.card.mark"),

    /**
     * Property key for -XX:+UseMembar.
     */
    WARN_USE_MEMBAR("warn.use.membar"),

    /**
     * Property key for disabling the Java Thread API with -XX:-UseThreadPriorities.
     */
    WARN_USE_THREAD_PRIORITIES_DISABLED("warn.use.thread.priorities.disabled"),

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
