import {Box, Button} from "@mui/material"
import {FC} from "react"

interface PhotoPaginationInterface {
    hasNextValues: boolean;
    hasPreviousValues: boolean;
    onPreviousClick: () => void;
    onNextClick: () => void;
}
export const PhotoPagination: FC<PhotoPaginationInterface> = ({hasNextValues, hasPreviousValues, onPreviousClick, onNextClick}) => {
    return (
        <Box sx={{
            display: "flex",
            width: "100%",
            alignItems: "center",
            justifyContent: "flex-end",
        }}
             data-test-id={"photoPagination"}
        >
            <Button type="button"
                    sx={{
                        margin: 2,
                        width: 100,
                    }}
                    variant="outlined"
                    disabled={!hasPreviousValues}
                    onClick={onPreviousClick}
            >
                Previous
            </Button>
            <Button type="button"
                    sx={{
                        margin: 2,
                        width: 100,
                    }}
                    variant="outlined"
                    disabled={!hasNextValues}
                    onClick={onNextClick}
            >
                Next
            </Button>

        </Box>
    )
}