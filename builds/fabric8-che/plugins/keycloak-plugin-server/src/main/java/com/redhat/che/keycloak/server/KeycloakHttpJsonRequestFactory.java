/*
 * Copyright (c) 2016-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package com.redhat.che.keycloak.server;

import com.redhat.che.keycloak.server.oso.service.account.ServiceAccountInfoProvider;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import org.eclipse.che.api.core.rest.DefaultHttpJsonRequestFactory;
import org.eclipse.che.api.core.rest.HttpJsonRequest;
import org.eclipse.che.api.core.rest.shared.dto.Link;

@Singleton
public class KeycloakHttpJsonRequestFactory extends DefaultHttpJsonRequestFactory {

  private boolean keycloakEnabled;

  @Inject ServiceAccountInfoProvider serviceAccountInfoProvider;

  @Inject
  public KeycloakHttpJsonRequestFactory(@Named("che.keycloak.enabled") boolean keycloakEnabled) {
    this.keycloakEnabled = keycloakEnabled;
  }

  @Override
  public HttpJsonRequest fromUrl(@NotNull String url) {
    if (!keycloakEnabled) {
      return super.fromUrl(url);
    }
    String token = serviceAccountInfoProvider.getToken();
    return super.fromUrl(url).setAuthorizationHeader("Wsagent " + token);
  }

  @Override
  public HttpJsonRequest fromLink(@NotNull Link link) {
    if (!keycloakEnabled) {
      return super.fromLink(link);
    }
    String token = serviceAccountInfoProvider.getToken();
    return super.fromLink(link).setAuthorizationHeader("Wsagent " + token);
  }
}
