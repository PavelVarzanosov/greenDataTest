package com.greenData.bank.matchers;

import com.greenData.bank.models.Client;
import org.mockito.ArgumentMatcher;

public class ClientMatcher implements ArgumentMatcher<Client> {

    private Client left;

    public ClientMatcher(Client updatedClient) {
        left = updatedClient;
    }

    @Override
    public boolean matches(Client right) {
        return left.getClientId().equals(right.getClientId()) &&
                left.getAddress().equals(right.getAddress()) &&
                left.getLegalForm().equals(right.getLegalForm()) &&
                left.getName().equals(right.getName()) &&
                left.getShortName().equals(right.getShortName());
    }
}
