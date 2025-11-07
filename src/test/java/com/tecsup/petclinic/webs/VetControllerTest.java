package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VetDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for VetController
 * @author jgomezm
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test find all vets
     * @throws Exception
     */
    @Test
    public void testFindAllVets() throws Exception {
        final int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    /**
     * Test find vet by id - OK
     * @throws Exception
     */
    @Test
    public void testFindVetOK() throws Exception {
        String FIRST_NAME = "James";
        String LAST_NAME = "Carter";

        this.mockMvc.perform(get("/vets/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)));
    }

    /**
     * Test find vet by id - KO (not found)
     * @throws Exception
     */
    @Test
    public void testFindVetKO() throws Exception {
        mockMvc.perform(get("/vets/666"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test create vet
     * @throws Exception
     */
    @Test
    public void testCreateVet() throws Exception {
        String FIRST_NAME = "Maria";
        String LAST_NAME = "Rodriguez";

        VetDTO newVetDTO = VetDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        this.mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)));
    }

    /**
     * Test delete vet
     * @throws Exception
     */
    @Test
    public void testDeleteVet() throws Exception {
        String FIRST_NAME = "Carlos";
        String LAST_NAME = "Mendez";

        VetDTO newVetDTO = VetDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }

    /**
     * Test delete vet - KO (not found)
     * @throws Exception
     */
    @Test
    public void testDeleteVetKO() throws Exception {
        mockMvc.perform(delete("/vets/1000"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test update vet
     * @throws Exception
     */
    @Test
    public void testUpdateVet() throws Exception {
        String FIRST_NAME = "Pedro";
        String LAST_NAME = "Sanchez";
        String UP_FIRST_NAME = "Pablo";
        String UP_LAST_NAME = "Garcia";

        VetDTO newVetDTO = VetDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // UPDATE
        VetDTO upVetDTO = VetDTO.builder()
                .id(id)
                .firstName(UP_FIRST_NAME)
                .lastName(UP_LAST_NAME)
                .build();

        mockMvc.perform(put("/vets/" + id)
                        .content(om.writeValueAsString(upVetDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND
        mockMvc.perform(get("/vets/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)));

        // DELETE
        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }

}