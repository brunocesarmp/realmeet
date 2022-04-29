package br.com.sw2you.realmeet.report.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.report.enumeration.ReportFormat;
import java.util.Arrays;
import java.util.Objects;

public class GeneratedReport {

    private final byte[] bytes;
    private final ReportFormat reportFormat;
    private final String fileName;
    private final String email;
    private final TemplateType templateType;

    private GeneratedReport(Builder builder) {
        this.bytes = builder.bytes;
        this.reportFormat = builder.reportFormat;
        this.fileName = builder.fileName;
        this.email = builder.email;
        this.templateType = builder.templateType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEmail() {
        return email;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedReport that = (GeneratedReport) o;
        return (
                Arrays.equals(bytes, that.bytes) &&
                        reportFormat == that.reportFormat &&
                        Objects.equals(fileName, that.fileName) &&
                        Objects.equals(email, that.email) &&
                        templateType == that.templateType
        );
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(reportFormat, fileName, email, templateType);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bytes", bytes)
                .append("reportFormat", reportFormat)
                .append("fileName", fileName)
                .append("email", email)
                .append("templateType", templateType)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private byte[] bytes;
        private ReportFormat reportFormat;
        private String fileName;
        private String email;
        private TemplateType templateType;

        private Builder() {
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder reportFormat(ReportFormat reportFormat) {
            this.reportFormat = reportFormat;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder templateType(TemplateType templateType) {
            this.templateType = templateType;
            return this;
        }

        public GeneratedReport build() {
            return new GeneratedReport(this);
        }
    }

}
