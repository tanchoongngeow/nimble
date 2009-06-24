/*
 *  Nimble, an extensive application base for Grails
 *  Copyright (C) 2009 Intient Pty Ltd
 *
 *  Open Source Use - GNU Affero General Public License, version 3
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Commercial/Private Use
 *
 *  You may purchase a commercial version of this software which
 *  frees you from all restrictions of the AGPL by visiting
 *  http://intient.com/products/nimble/licenses
 *
 *  If you have purchased a commercial version of this software it is licensed
 *  to you under the terms of your agreement made with Intient Pty Ltd.
 */
package intient.nimble.domain

import grails.test.*

/**
 * @author Bradley Beddoes
 */
class RoleTests extends GrailsUnitTestCase {

    def description 
    def name 
    def protect 

    def user1 
    def user2 
    def user3 

    def group1 
    def group2 

    def dateCreated 
    def lastUpdated 

    protected void setUp() {
        super.setUp()

        description = 'description'
        name = 'name'
        protect = true

        user1 = new User()
        user2 = new User()
        user3 = new User()

        group1 = new Group()
        group2 = new Group()

        dateCreated = new Date()
        lastUpdated = new Date()
    }

    protected void tearDown() {
        super.tearDown()
    }

    Role createValidRole() {
        def role = new Role(description:description, name:name, protect:protect,
            users:[user1,user2,user3], groups:[group1,group2],
            dateCreated:dateCreated, lastUpdated:lastUpdated)

        return role
    }

    void testRoleCreation() {
        def role = createValidRole()

        assertEquals description, role.description
        assertEquals name, role.name
        assertEquals protect, role.protect
        assertTrue role.users.containsAll([user1,user2,user3])
        assertTrue role.groups.containsAll([group1,group2])
    }

    void testNameConstraint() {
        mockForConstraintsTests(Role)
        def role = createValidRole()

        assertTrue role.validate()

        role.name = ''
        assertFalse role.validate()

        role.name = null
        assertFalse role.validate()

        // must be unique
        def role2 = createValidRole()
        role2.name = 'name2'
        role.name = name
        mockForConstraintsTests(Role, [role, role2])

        assertTrue role.validate()
        assertTrue role2.validate()

        role2.name = name
        assertFalse role.validate()
        assertFalse role2.validate()

        role2.name = 'name2'
        assertTrue role.validate()
        assertTrue role2.validate()

        //min size
        role.name = '123'
        assertFalse role.validate()

        //max size
        role.name = 'abcd'.center(532)
        assertFalse role.validate()
    }

    void testDescriptionConstraint() {
        mockForConstraintsTests(Role)
        def role = createValidRole()

        assertTrue role.validate()

        role.description = ''
        assertFalse role.validate()

        role.description = null
        assertTrue role.validate()
    }

    void testDateCreatedConstraint() {
        mockForConstraintsTests(Role)

        def role = createValidRole()
        assertTrue role.validate()

        role.dateCreated = null
        assertTrue role.validate()
    }

    void testLastUpdatedConstraint() {
        mockForConstraintsTests(Role)

        def role = createValidRole()
        assertTrue role.validate()

        role.lastUpdated = null
        assertTrue role.validate()
    }
}