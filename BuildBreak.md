
1. FAILURE: Build failed with an exception.

* What went wrong:
  loader constraint violation in interface itable initialization for class com.android.build.gradle.internal.api.VariantFilter: when selecting method 'com.android.builder.model.BuildType com.android.build.api.variant.VariantFilter.getBuildType()' the class loader org.gradle.internal.classloader.VisitableURLClassLoader @1ad4b958 for super interface com.android.build.api.variant.VariantFilter, and the class loader org.gradle.internal.classloader.VisitableURLClassLoader @49cfb38a of the selected method's class, com.android.build.gradle.internal.api.VariantFilter have different Class objects for the type com.android.builder.model.BuildType used in the signature (com.android.build.api.variant.VariantFilter is in unnamed module of loader org.gradle.internal.classloader.VisitableURLClassLoader @1ad4b958, parent loader org.gradle.internal.classloader.CachingClassLoader @5a812a0c; com.android.build.gradle.internal.api.VariantFilter is in unnamed module of loader org.gradle.internal.classloader.VisitableURLClassLoader @49cfb38a, parent loader org.gradle.internal.classloader.VisitableURLClassLoader @1ad4b958)


> Solution : restart pc

2. 
