/*
 * Copyright 2014 - 2023 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.persistence.view.spi.type;

/**
 * A base class for implementing basic user types for non-entity mutable types.
 *
 * @param <X> The type of the user type
 * @author Christian Beikov
 * @since 1.2.0
 */
public abstract class AbstractMutableBasicUserType<X> implements BasicUserType<X> {

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean supportsDeepEqualChecking() {
        return true;
    }

    @Override
    public boolean supportsDeepCloning() {
        return true;
    }

    @Override
    public boolean isEqual(X initial, X current) {
        return isDeepEqual(initial, current);
    }

    @Override
    public boolean isDeepEqual(X object1, X object2) {
        return object1.equals(object2);
    }

    @Override
    public int hashCode(X object) {
        return object.hashCode();
    }

    @Override
    public boolean supportsDirtyChecking() {
        return false;
    }

    @Override
    public boolean supportsDirtyTracking() {
        return false;
    }

    @Override
    public boolean shouldPersist(X entity) {
        return false;
    }

    @Override
    public String[] getDirtyProperties(X entity) {
        return DIRTY_MARKER;
    }

    @Override
    public X fromString(CharSequence sequence) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toStringExpression(String expression) {
        throw new UnsupportedOperationException();
    }
}
