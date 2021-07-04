package com.epam.esm.common_service;

import com.epam.esm.model.Tag;

import java.util.List;

public interface CustomTagServise<Tag> {

    List<Tag> getMostUsedTagsOfUserWithMostExpensiveOrdersCost();

}
