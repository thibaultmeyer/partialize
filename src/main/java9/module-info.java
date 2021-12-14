module com.zero_x_baadf00d.partialize {
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;

    exports com.zero_x_baadf00d.partialize.annotation;
    exports com.zero_x_baadf00d.partialize.converter;
    exports com.zero_x_baadf00d.partialize.policy;
    exports com.zero_x_baadf00d.partialize;
}
