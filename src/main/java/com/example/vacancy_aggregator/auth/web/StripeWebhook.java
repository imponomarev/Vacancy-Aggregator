package com.example.vacancy_aggregator.auth.web;

import com.example.vacancy_aggregator.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/stripe")
@RequiredArgsConstructor
public class StripeWebhook {
    @Value("${stripe.endpoint-secret}")
    String endpointSecret;
    private final UserRepository repo;

    @PostMapping
    public void handle(@RequestBody String payload,
                       @RequestHeader("Stripe-Signature") String sig) throws Exception {

        Event ev = Webhook.constructEvent(payload, sig, endpointSecret);
        if ("checkout.session.completed".equals(ev.getType())) {
            Session sess = (Session) ev.getDataObjectDeserializer().getObject().get();
            Long userId = Long.valueOf(sess.getClientReferenceId());
            repo.findById(userId).ifPresent(u -> u.setRole(User.Role.PRO));
        }
    }
}

