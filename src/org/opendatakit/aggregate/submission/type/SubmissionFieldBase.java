/*
 * Copyright (C) 2009 Google Inc. 
 * Copyright (C) 2010 University of Washington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.aggregate.submission.type;



import org.opendatakit.aggregate.constants.ErrorConsts;
import org.opendatakit.aggregate.datamodel.FormDataModel;
import org.opendatakit.aggregate.datamodel.FormDataModel.ElementType;
import org.opendatakit.aggregate.exception.ODKConversionException;
import org.opendatakit.aggregate.format.element.ElementFormatter;
import org.opendatakit.aggregate.format.element.Row;
import org.opendatakit.aggregate.submission.SubmissionField;
import org.opendatakit.common.constants.BasicConsts;
import org.opendatakit.common.persistence.CommonFieldsBase;
import org.opendatakit.common.persistence.Datastore;
import org.opendatakit.common.persistence.EntityKey;
import org.opendatakit.common.persistence.exception.ODKDatastoreException;
import org.opendatakit.common.security.User;


public abstract class SubmissionFieldBase<T> implements SubmissionField<T>{

  /**
   * Submission property/element name
   */
  protected FormDataModel element;

  public SubmissionFieldBase(FormDataModel element) {
    this.element = element;
  }

  /**
   * Get Property Name
   *
   * @return
   *    property name
   */
  public final String getPropertyName() {
    return element.getElementName();
  }
  
  /**
   * Get the value of submission field
   * 
   * @return
   *    value
   */
  public abstract T getValue();
  
  /**
   * Parse the value from string format and convert to proper type for
   * submission field
   * 
   * @param value string form of the value
   * @throws ODKConversionException
 * @throws ODKDatastoreException 
   */
  public abstract void setValueFromString(String value) throws ODKConversionException, ODKDatastoreException;
  
  
  /**
   * Get submission field value from database entity
   *
   * @param dbEntity entity to obtain value
   * @param formDefinition the form definition object
 * @throws ODKDatastoreException 
   */
  public abstract void getValueFromEntity(CommonFieldsBase dbEntity, String uriAssociatedRow,
		  									EntityKey topLevelTableKey, 
		  									Datastore datastore, User user, boolean fetchElement)
  					throws ODKDatastoreException;
  
  /**
   * Add submission field value to JsonObject
   * @param JSON Object to add value to
   */  
  public abstract void formatValue(ElementFormatter elemFormatter, Row row) throws ODKDatastoreException;
  
  @Override
  public final boolean isBinary() {
	  return (element.getElementType() == ElementType.REF_BLOB);
  }
  
  /**
   * Convert byte array to proper type for submission field
   * 
   * @param byteArray byte form of the value
   * @param submissionSetKey key of submission set that will reference the blob
   * @param contentType type of binary data (NOTE: only used for binary data)
   * @throws ODKConversionException
 * @throws ODKDatastoreException 
   * 
   */ 
  @Override
  public void setValueFromByteArray(byte [] byteArray, String contentType, Long contentLength, String unrootedFilePath, Datastore datastore, User user) throws ODKConversionException, ODKDatastoreException {
    if(isBinary()) {
      throw new IllegalStateException("Should be overridden in derived class");
    } else {
      throw new ODKConversionException(ErrorConsts.BINARY_ERROR);
    }
  }
  
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof SubmissionFieldBase<?>)) {
      return false;
    }
    
    SubmissionFieldBase<?> other = (SubmissionFieldBase<?>) obj;
    return (element == null ? (other.element == null) : (element.equals(other.element)));    
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 13;
    if(element != null) hashCode += element.hashCode();
    return hashCode; 
  }
  
  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return (element != null ? element.getElementName() : BasicConsts.EMPTY_STRING); 
  }
}