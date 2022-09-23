import React, { useRef, useState } from "react";
import MyInterestModal from "./MyInterestModal";
import MyInterestItemList from "./MyInterestItemList";

import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";

export default function MyInerest() {
  // 임시 데이터
  const [interests, setInterests] = useState([]);
  const [updateInts, setUpdateInts] = useState([]);

  const nextId = useRef(0);
  const handleSumit = (nation, interestCurrency) => {
    const interest = {
      id: nextId.current,
      nation,
      interestCurrency,
    };
    setInterests(interests.concat(interest));
    nextId.current += 1;
  };

  // const onUpdate = (nation, interestCurrency) => {
  //   const updateInt = {
  //     nation,
  //     interestCurrency,
  //   };
  //   setUpdateInts(updateInts.concat(updateInt));
  // };

  // 삭제 기능
  const onRemove = (id) => {
    setInterests(interests.filter((interest) => interest.id !== id));
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Typography id="font_test" component="h1" variant="h4">
        관심 화폐 등록
      </Typography>
      <br />
      <MyInterestModal onSubmit={handleSumit} />
      <br />
      <MyInterestItemList
        interests={interests}
        onRemove={onRemove}
        // onUpdate={onUpdate}
      />
    </Box>
  );
}