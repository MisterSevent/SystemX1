package com.system.integration;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Integrations {

    private static List<Integration> enabledIntegrations = new ArrayList<Integration>();

    public static void add(Integration... integrations) {
        for (Integration integration : integrations) {
            add(integration);
        }
    }

    public static void add(Integration integration) {
        if (integration.isEnabled()) {
            integration.init();
            enabledIntegrations.add(integration);
        } else {
            integration.notifyDisabled();
        }
    }

    public static List<Integration> getAll() {
        return Lists.newArrayList(enabledIntegrations);
    }

}
