package br.com.vitortheof.checkin.utils;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            .allowElements("p", "br", "strong", "em", "u", "s")
            .allowElements("h1", "h2", "h3")
            .allowElements("ul", "ol", "li")
            .allowElements("a")
            .allowAttributes("href").onElements("a")
            .allowStandardUrlProtocols() // http:, https:, mailto:
            .requireRelNofollowOnLinks() // adiciona rel="nofollow" automaticamente
            .toFactory();
    public static String sanitizar(String htmlBruto) {
        if (htmlBruto == null) return null;
        return POLICY.sanitize(htmlBruto);
    }
}