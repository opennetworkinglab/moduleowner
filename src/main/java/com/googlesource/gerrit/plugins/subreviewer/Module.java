package com.googlesource.gerrit.plugins.subreviewer;

import com.google.gerrit.common.EventListener;
import com.google.gerrit.extensions.events.GitReferenceUpdatedListener;
import com.google.gerrit.extensions.events.UsageDataPublishedListener;
import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.gerrit.server.config.FactoryModule;
import com.google.gerrit.server.extensions.events.GitReferenceUpdated;
import com.google.gerrit.server.git.validators.MergeValidationListener;

import static com.google.gerrit.server.change.RevisionResource.REVISION_KIND;

class Module extends FactoryModule {
  @Override
  protected void configure() {
      DynamicSet.bind(binder(), MergeValidationListener.class)
              .to(MergeUserValidator.class);
      DynamicSet.bind(binder(), EventListener.class).to(
              ChangeEventListener.class);
      DynamicSet.bind(binder(), UsageDataPublishedListener.class).to(
              UsageDataListener.class);

      // TODO finish implementation of DynamicSubmit and re-enable
//      bind(CapabilityDefinition.class)
//              .annotatedWith(Exports.named(DynamicSubmitCapability.DYANMIC_SUBMIT))
//              .to(DynamicSubmitCapability.class);

      install(new RestApiModule() {
          @Override
          protected void configure() {
              get(REVISION_KIND, "file-owner").to(FileOwner.class);
          }
      });



      install(ModuleOwnerConfigCacheImpl.module());

      factory(ModuleOwnerConfig.Factory.class);
      factory(ReviewersByOwnership.Factory.class);
  }
}
