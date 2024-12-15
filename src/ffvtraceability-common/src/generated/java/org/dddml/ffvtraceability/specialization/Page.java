package org.dddml.ffvtraceability.specialization;

import java.util.List;

/**
 * Page of content(list).
 */
public interface Page<T> {

    /**
     * Returns a new builder instance.
     * This method requires explicit type parameter.
     */
    static <T> PageBuilder<T> builder() {
        return new PageBuilder<>();
    }

    /**
     * Returns a new builder instance with initial content.
     * Type parameter is inferred from content list.
     */
    static <T> PageBuilder<T> builder(List<T> content) {
        return new PageBuilder<T>().content(content);
    }

    /**
     * Returns the page content as {@link List}.
     *
     * @return
     */
    List<? extends T> getContent();

    /**
     * Returns the total amount of elements.
     *
     * @return the total amount of elements
     */
    long getTotalElements();

    /**
     * Returns the size of the page.
     *
     * @return the size of the page.
     */
    int getSize();

    /**
     * Returns the number of the current page. Is always non-negative.
     *
     * @return the number of the current page.
     */
    int getNumber();

    /**
     * Returns whether there is a next page.
     */
    default boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    /**
     * Returns whether there is a previous page.
     */
    default boolean hasPrevious() {
        return getNumber() > 0;
    }

    /**
     * Returns the total amount of pages.
     */
    default int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) getTotalElements() / (double) getSize());
    }

    /**
     * Returns if the page has content.
     */
    default boolean hasContent() {
        return getContent() != null && !getContent().isEmpty();
    }

    /**
     * Builder class for creating {@link Page} instances.
     */
    class PageBuilder<T> {
        private List<T> content;
        private long totalElements;
        private int size;
        private int number;

        private PageBuilder() {
        }

        /**
         * Sets the content of the page.
         *
         * @param content the content of the page
         * @return this builder instance
         */
        public PageBuilder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        /**
         * Sets the total amount of elements.
         *
         * @param totalElements the total amount of elements
         * @return this builder instance
         */
        public PageBuilder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        /**
         * Sets the size of the page.
         *
         * @param size the size of the page
         * @return this builder instance
         */
        public PageBuilder<T> size(int size) {
            this.size = size;
            return this;
        }

        /**
         * Sets the number of the current page.
         *
         * @param number the number of the current page
         * @return this builder instance
         */
        public PageBuilder<T> number(int number) {
            this.number = number;
            return this;
        }

        /**
         * Builds a new {@link Page} instance.
         *
         * @return a new {@link Page} instance
         */
        public Page<T> build() {
            PageImpl<T> page = new PageImpl<>();
            page.setContent(content);
            page.setTotalElements(totalElements);
            page.setSize(size);
            page.setNumber(number);
            return page;
        }
    }

    class PageImpl<T> implements Page<T> {

        private List<T> content;
        private long totalElements;
        private int size;
        private int number;

        public PageImpl() {
        }

        public PageImpl(List<T> content, long totalElements) {
            this.content = content;
            this.totalElements = totalElements;
        }

        @Override
        public List<? extends T> getContent() {
            return content;
        }

        public void setContent(List<T> content) {
            this.content = content;
        }

        @Override
        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        @Override
        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }


    }
}
